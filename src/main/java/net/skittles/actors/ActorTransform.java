package net.skittles.actors;

import net.skittles.maths.*;
import net.skittles.utility.Pair;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ActorTransform
{
    private final Actor owner;
    private Matrix4 transform;

    private ActorTransform parent;
    private final ArrayList<ActorTransform> children = new ArrayList<>();
    private final ArrayList<Pair<ActorTransform, Consumer<ActorTransform>>> childListChanges = new ArrayList<>();

    ActorTransform(Actor owner)
    {
        this.owner     = owner;
        this.transform = Matrix4.identity();
        this.parent    = null;
    }

    // -------------------------------------------------------------------------
    // Local Space
    // -------------------------------------------------------------------------

    public void setLocalLocation(Vector3 location)
    {
        this.transform.setTranslation(location.x(), location.y(), location.z());
    }

    public void setLocalRotation(Quaternion quaternion)
    {
        Vector3 scale    = localScale();
        Matrix4 rotation = quaternion.normalised().toMatrix4();
        this.transform.setColumn(0, new Vector4(rotation.getColumn(0).x() * scale.x(), rotation.getColumn(0).y() * scale.x(), rotation.getColumn(0).z() * scale.x(), 0.0F));
        this.transform.setColumn(1, new Vector4(rotation.getColumn(1).x() * scale.y(), rotation.getColumn(1).y() * scale.y(), rotation.getColumn(1).z() * scale.y(), 0.0F));
        this.transform.setColumn(2, new Vector4(rotation.getColumn(2).x() * scale.z(), rotation.getColumn(2).y() * scale.z(), rotation.getColumn(2).z() * scale.z(), 0.0F));
    }

    public void setLocalRotation(Vector3 eulerAngles)
    {
        setLocalRotation(Quaternion.fromEuler(eulerAngles.x(), eulerAngles.y(), eulerAngles.z()));
    }

    public void setLocalScale(Vector3 scale)
    {
        this.transform.setScale(scale.x(), scale.y(), scale.z());
    }

    public void setLocalScale(float scale)
    {
        setLocalScale(new Vector3(scale, scale, scale));
    }

    public Vector3 localLocation()
    {
        return this.transform.getTranslation();
    }

    public Quaternion localRotation()
    {
        return Quaternion.fromMatrix4(this.transform);
    }

    public Vector3 localEulerAngles()
    {
        return localRotation().toEuler();
    }

    public Vector3 localScale()
    {
        return this.transform.getScale();
    }

    // -------------------------------------------------------------------------
    // Global Space
    // -------------------------------------------------------------------------

    public void setLocation(Vector3 location)
    {
        if (this.parent == null)
        {
            setLocalLocation(location);
        }
        else
        {
            // Convert global location to local space by inverting the parent transform
            Matrix4 parentInverse = this.parent.globalTransform().inverse();
            Vector4 local         = parentInverse.multiply(new Vector4(location.x(), location.y(), location.z(), 1.0F));
            setLocalLocation(new Vector3(local.x(), local.y(), local.z()));
        }
    }

    public void setRotation(Quaternion quaternion)
    {
        if (this.parent == null)
        {
            setLocalRotation(quaternion);
        }
        else
        {
            // Convert global rotation to local by multiplying by the inverse parent rotation
            Quaternion parentInverse = this.parent.rotation().inverse();
            setLocalRotation(parentInverse.multiply(quaternion));
        }
    }

    public void setRotation(Vector3 eulerAngles)
    {
        setRotation(Quaternion.fromEuler(eulerAngles.x(), eulerAngles.y(), eulerAngles.z()));
    }

    public void setScale(Vector3 scale)
    {
        if (this.parent == null)
        {
            setLocalScale(scale);
        }
        else
        {
            // Convert global scale to local by dividing by parent scale
            Vector3 parentScale = this.parent.scale();
            setLocalScale(new Vector3(
                    parentScale.x() != 0.0F ? scale.x() / parentScale.x() : scale.x(),
                    parentScale.y() != 0.0F ? scale.y() / parentScale.y() : scale.y(),
                    parentScale.z() != 0.0F ? scale.z() / parentScale.z() : scale.z()
            ));
        }
    }

    public void setScale(float scale)
    {
        setScale(new Vector3(scale, scale, scale));
    }

    public Vector3 location()
    {
        return globalTransform().getTranslation();
    }

    public Quaternion rotation()
    {
        return Quaternion.fromMatrix4(globalTransform());
    }

    public Vector3 eulerAngles()
    {
        return rotation().toEuler();
    }

    public Vector3 scale()
    {
        return globalTransform().getScale();
    }

    public Matrix4 globalTransform()
    {
        return this.parent == null ? this.transform : this.parent.globalTransform().multiply(this.transform);
    }

    // -------------------------------------------------------------------------
    // Direction Vectors
    // -------------------------------------------------------------------------

    public Vector3 forward()
    {
        return rotation().rotate(new Vector3(0.0F, 0.0F, -1.0F)).normalised();
    }

    public Vector3 right()
    {
        return rotation().rotate(new Vector3(1.0F, 0.0F, 0.0F)).normalised();
    }

    public Vector3 up()
    {
        return rotation().rotate(new Vector3(0.0F, 1.0F, 0.0F)).normalised();
    }

    // -------------------------------------------------------------------------
    // Hierarchy
    // -------------------------------------------------------------------------

    public ActorTransform parent()
    {
        return this.parent;
    }

    public ArrayList<ActorTransform> children()
    {
        return new ArrayList<>(this.children);
    }

    public boolean hasParent()
    {
        return this.parent != null;
    }

    public boolean hasChildren()
    {
        return !this.children.isEmpty();
    }

    public void setParent(ActorTransform parent)
    {
        if (this.parent == parent)
        {
            return;
        }

        // Preserve global transform when re-parenting
        Matrix4 globalBefore = globalTransform();

        if (parent == null)
        {
            this.childListChanges.add(new Pair<>(
                    null,
                    (p) ->
                    {
                        this.parent.children.remove(this);
                        this.parent = null;
                        // Restore global transform in local space
                        this.transform = globalBefore;
                    }
            ));
        }
        else
        {
            this.childListChanges.add(new Pair<>(
                    parent,
                    (p) ->
                    {
                        if (this.parent != null)
                        {
                            this.parent.children.remove(this);
                        }

                        this.parent = parent;
                        this.parent.children.add(this);

                        // Convert old global transform into new local space
                        this.transform = parent.globalTransform().inverse().multiply(globalBefore);
                    }
            ));
        }
    }

    void applyChildListChanges()
    {
        for (Pair<ActorTransform, Consumer<ActorTransform>> child : this.childListChanges)
        {
            child.second.accept(child.first);
        }

        this.childListChanges.clear();
    }
}