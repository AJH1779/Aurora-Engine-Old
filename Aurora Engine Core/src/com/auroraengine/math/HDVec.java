/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.math;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * <code>HDVec</code>s are modifiable objects that denote a translation in
 * Cartesian space using double precision.
 * It is important to duplicate the object whenever making modifications that
 * are not to be reflected in the original.
 * @author Arthur
 */
public final class HDVec {
    // The Static Method classes
    /**
     * Returns the average of the vectors, that is the sum of all vectors
     * divided by the number of vectors provided.
     * @param vecs The vectors to average.
     * @return The average vector.
     */
    public static HDVec getAverage(HDVec... vecs) {
        HDVec avg = new HDVec();
        for(HDVec vec : vecs) {
            avg.translate(vec);
        }
        return avg.invscale(vecs.length);
    }
    /**
     * Returns the vector which denotes B - A, which is the translation vector
     * for moving from the point A to point B.
     * @param A The starting point
     * @param B The destination point
     * @return The translation vector
     */
    public static HDVec getDistVec(HDVec A, HDVec B) {
        return new HDVec(B).negTranslate(A);
    }
    /**
     * Returns the distance between A and B. This is equivalent to getting
     * the length of the vector produced from <code>getDistVec()</code>.
     * @param A The starting point
     * @param B The destination point
     * @return The separation
     */
    public static double getDist(HDVec A, HDVec B) {
        return Math.sqrt(getSqrDist(A, B));
    }
    /**
     * Returns the square distance between A and B. This is equivalent to getting
     * the square length of the vector produced from <code>getDistVec()</code>.
     * @param A The starting point
     * @param B The destination point
     * @return The separation squared
     */
    public static double getSqrDist(HDVec A, HDVec B) {
        return getDistVec(A, B).getSqrLen();
    }
    
    // The Local Method classes
    /**
     * Creates a new vector of zero length.
     */
    public HDVec() {}
    /**
     * Creates a new vector with the provided X and Y components and zero for
     * the Z component.
     * @param x The X component
     * @param y The Y component
     */
    public HDVec(double x, double y) { set(x, y, 0.0); }
    /**
     * Creates a new vector with the provided X, Y, and Z components
     * @param x The X component
     * @param y The Y component
     * @param z The Z component
     */
    public HDVec(double x, double y, double z) { set(x, y, z); }
    /**
     * Creates a new vector which has the same X, Y, and Z components as the
     * provided vector.
     * @param vec The vector to copy.
     */
    public HDVec(HDVec vec) { set(vec); }
    private final double[] data = new double[3];
    
    /**
     * Returns the X component of this vector.
     * @return The X component
     */
    public double X() { return data[0]; }
    /**
     * Returns the Y component of this vector.
     * @return The Y component
     */
    public double Y() { return data[1]; }
    /**
     * Returns the Z component of this vector.
     * @return The Z component
     */
    public double Z() { return data[2]; }
    /**
     * Returns the array which stores the X, Y, and Z components of this vector.
     * Modifying this modifies the components in this vector.
     * @return 
     */
    public double[] array() { return data; }
    /**
     * Places the X, Y, and Z components into the provided buffer in that order,
     * then returns the provided buffer.
     * @param bb The Buffer
     * @return The Provided Buffer
     */
    public ByteBuffer data(ByteBuffer bb)
    { bb.putDouble(data[0]); bb.putDouble(data[1]); bb.putDouble(data[2]); return bb; }
    
    /**
     * Sets the X component to the provided value, then returns this vector.
     * @param x The new X component
     * @return This
     */
    public HDVec X(double x) { data[0] = x; return this; }
    /**
     * Sets the Y component to the provided value, then returns this vector.
     * @param y The new Y component
     * @return This
     */
    public HDVec Y(double y) { data[1] = y; return this; }
    /**
     * Sets the Z component to the provided value, then returns this vector.
     * @param z The new Z component
     * @return This
     */
    public HDVec Z(double z) { data[2] = z; return this; }
    /**
     * Sets the X, Y, and Z components to the provided values, then returns this
     * vector.
     * @param x The new X component
     * @param y The new Y component
     * @param z The new Z component
     * @return This
     */
    public HDVec set(double x, double y, double z)
    { data[0] = x; data[1] = y; data[2] = z; return this; }
    /**
     * Sets the X, Y, and Z components to those of the provided vector, then
     * returns this vector.
     * @param v The vector to copy
     * @return This
     */
    public HDVec set(HDVec v)
    { data[0] = v.data[0]; data[1] = v.data[1]; data[2] = v.data[2]; return this; }
    /**
     * Returns the length of this vector. If used for comparison, try using the
     * faster <code>sqrLength()</code> method.
     * @return The length
     */
    public double getLen() { return Math.sqrt(dot(this)); }
    /**
     * Returns the square length of this vector. This is faster than finding the
     * length.
     * @return The square length
     */
    public double getSqrLen() { return dot(this); }
    
    /**
     * Adds the provided X, Y, and Z values to the corresponding components
     * in this vector, then returns this. This has the effect of translating 
     * this by the specified amount in each component.
     * @param x The X component to add
     * @param y The Y component to add
     * @param z The Z component to add
     * @return This
     */
    public HDVec translate(double x, double y, double z)
    { data[0] += x; data[1] += y; data[2] += z; return this; }
    /**
     * Adds the provided vector to this vector, then returns this. This has the
     * effect of translating this by the specified vector.
     * @param v The translation vector
     * @return This
     */
    public HDVec translate(HDVec v)
    { data[0] += v.data[0]; data[1] += v.data[1]; data[2] += v.data[2]; return this; }
    /**
     * Subtracts the provided vector to this vector, then returns this. This has
     * the effect of translating this by the negative of the specified vector.
     * @param v The translation vector
     * @return This
     */
    public HDVec negTranslate(HDVec v)
    { data[0] -= v.data[0]; data[1] -= v.data[1]; data[2] -= v.data[2]; return this; }
    /**
     * Scales all components in this vector by the specified scale factor, then
     * returns this.
     * @param s The scale factor
     * @return This
     */
    public HDVec scale(double s)
    { data[0] *= s; data[1] *= s; data[2] *= s; return this; }
    /**
     * Scales all components in this vector by the inverse of the specified
     * scale factor, then returns this.
     * @param s The inverse of the scale factor
     * @return This
     */
    public HDVec invscale(double s)
    { return scale(1.0f / s); }
    
    /**
     * Returns the dot product of this vector and the specified vector.
     * @param v The second vector
     * @return The dot product
     */
    public double dot(HDVec v)
    { return data[0] * v.data[0] + data[1] * v.data[1] + data[2] * v.data[2]; }
    /**
     * Returns the cross product of this vector and the specified vector as a
     * new vector. The order of arguments is as written, so <code>A.cross(B)</code>
     * is equivalent to A × B.
     * @param v The second vector
     * @return The cross product vector
     */
    public HDVec cross(HDVec v)
    { return new HDVec(data[1] * v.data[2] - data[2] * v.data[1],
                       data[2] * v.data[0] - data[0] * v.data[2],
                       data[0] * v.data[1] - data[1] * v.data[0]); }
    
    /**
     * Normalises this vector (sets the length to 1), then returns this.
     * @return This
     */
    public HDVec normalise() { return invscale((double) getLen()); }
    /**
     * Scales this vector by -1, then returns this.
     * @return This
     */
    public HDVec negative() { return scale(-1.0f); }
    /**
     * Reflects this vector through the plane defined by the provided normal
     * vector, then returns this. If the provided vector is not normalised, the
     * result will be scaled by the same amount as the provided vector length.
     * @param n The plane normal vector.
     * @return This
     */
    public HDVec reflect(HDVec n) {
        return set(
            (1.0 - 2.0 * n.data[0] * n.data[0]) * data[0]        - 2.0 * n.data[0] * n.data[1]  * data[1]        - 2.0 * n.data[0] * n.data[2]  * data[2],
                 - 2.0 * n.data[0] * n.data[1]  * data[0] + (1.0 - 2.0 * n.data[1] * n.data[1]) * data[1]        - 2.0 * n.data[1] * n.data[2]  * data[2],
                 - 2.0 * n.data[0] * n.data[2]  * data[0]        - 2.0 * n.data[1] * n.data[2]  * data[1] + (1.0 - 2.0 * n.data[2] * n.data[2]) * data[2]);
    }
    
    /**
     * Returns a copy of this vector as a <code>LDVec</code>.
     * @return A copy of This
     */
    public LDVec toLD() { return new LDVec((float) data[0], (float) data[1],
            (float) data[2]); }
    /**
     * Returns a copy of this vector.
     * @return A copy of This
     */
    public HDVec toHD() { return new HDVec(this); }
    
    /**
     * Returns this vector as a string in the format "(x,y,z)", where x, y, and
     * z are the corresponding components of this vector.
     * @return A string representation of this vector.
     */
    @Override
    public String toString() {
        return "(" + data[0] + "," + data[1] + "," + data[2] + ")";
    }
    /**
     * Returns true if the provided object is a <code>HDVec</code> with the
     * same components as this. Use a comparison of square separation with some
     * tolerance to determine when two vectors are similar
     * @param obj The object to check
     * @return True if the provided object is the same as this.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof HDVec && Arrays.equals(data, ((HDVec) obj).data);
    }
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Arrays.hashCode(this.data);
        return hash;
    }
}