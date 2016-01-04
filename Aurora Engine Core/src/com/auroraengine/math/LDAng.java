/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.math;

/**
 * <code>LDAng</code>s are modifiable objects that denote a rotation in
 * Cartesian space using float precision.
 * It is important to duplicate the object whenever making modifications that
 * are not to be reflected in the original.
 * @author Arthur
 */
public final class LDAng {
    // The Static Method classes
    
    // The Local Method classes
    /**
     * Creates a new zero rotation object.
     */
    public LDAng() {}
    /**
     * Creates a new rotation object on the clockwise on the XY plane.
     * @param ang The rotation angle
     */
    public LDAng(float ang) { set(ang); }
    /**
     * Creates a new rotation object around the specified vector by the
     * provided angle, which should be normalised.
     * @param x The x vector component
     * @param y The y vector component
     * @param z The z vector component
     * @param ang The rotation angle in radians
     */
    public LDAng(float x, float y, float z, float ang) { set(x, y, z, ang); }
    /**
     * Creates a new rotation object around the specified vector by the provided
     * angle, which should be normalised.
     * @param vec The vector
     * @param ang The rotation angle in radians
     */
    public LDAng(LDVec vec, float ang) { set(vec, ang); }
    /**
     * Creates a new rotation object which is a copy of the provided object.
     * @param ang The rotation object.
     */
    public LDAng(LDAng ang) { set(ang); }
    private final LDVec vec = new LDVec(0.0f, 0.0f, 1.0f);
    private final float[] dat = new float[3];
    
    /**
     * Sets the X, Y, and Z components of the rotation vector to the provided
     * values, then returns this.
     * @param x The new X component
     * @param y The new Y component
     * @param z The new Z component
     * @return This
     */
    public LDAng set(float x, float y, float z) { this.vec.set(x, y, z); return this; }
    /**
     * Sets the X, Y, and Z components of the rotation vector to those of the
     * provided vector, then returns this.
     * @param v The vector to copy
     * @return This
     */
    public LDAng set(LDVec vec) { this.vec.set(vec); return this; }
    /**
     * Sets the rotation angle to the specified amount, then returns this.
     * @param ang The new rotation angle
     * @return This
     */
    public LDAng set(float ang)
    { dat[0] = ang; dat[1] = (float) Math.sin(ang); dat[2] = (float) Math.cos(ang); return this; }
    /**
     * 
     * Sets the X, Y, and Z components of the rotation vector to the provided
     * values and the rotation angle to the specified amount, then returns this.
     * @param x The new X component
     * @param y The new Y component
     * @param z The new Z component
     * @param ang The new rotation angle
     * @return This
     */
    public LDAng set(float x, float y, float z, float ang)
    { set(x, y, z); return set(ang);}
    /**
     * Sets the X, Y, and Z components of the rotation vector to those of the
     * provided vector and the rotation angle to the specified amount, then
     * returns this.
     * @param v The vector to copy
     * @param ang The new rotation angle
     * @return This
     */
    public LDAng set(LDVec vec, float ang) { set(vec); return set(ang); }
    /**
     * Sets this object to have the same vector and angle as the provided angle,
     * then returns this.
     * @param ang The angle to copy
     * @return This
     */
    public LDAng set(LDAng ang)
    { dat[0] = ang.dat[0]; dat[1] = ang.dat[1]; dat[2] = ang.dat[2]; return set(ang.vec); }
    
    /**
     * Returns the X component of the rotation vector.
     * @return The X component
     */
    public float X() { return vec.X(); }
    /**
     * Returns the Y component of the rotation vector.
     * @return The Y component
     */
    public float Y() { return vec.Y(); }
    /**
     * Returns the Z component of the rotation vector.
     * @return The Z component
     */
    public float Z() { return vec.Z(); }
    /**
     * Returns the rotation vector. Modifying this vector modifies the rotation
     * vector in the angle object.
     * @return The rotation vector
     */
    public LDVec vec() { return vec; }
    /**
     * Returns the rotation angle.
     * @return 
     */
    public float ang() { return dat[0]; }
    /**
     * Returns the sine of the rotation angle.
     * @return 
     */
    public float sin() { return dat[1]; }
    /**
     * Returns the cosine of the rotation angle.
     * @return 
     */
    public float cos() { return dat[2]; }
}
