/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.math;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.util.Arrays;

/**
 *
 * @author Arthur
 */
public final class HDRef {
    // The Static Methods
    private static final double[] IDENTITY = new double[]
    {1., 0., 0., 0.,  0., 1., 0., 0.,  0., 0., 1., 0.,  0., 0., 0., 1.};
    private static final double[] ZERO = new double[16];
    
    /**
     * Performs the matrix multiplication of ref1 on ref2, returning the result
     * as a new reference matrix.
     * @param ref1 The inner reference frame
     * @param ref2 The second reference frame
     * @return The final reference frame
     */
    public static HDRef mult(HDRef ref1, HDRef ref2) {
        return mult(ref1, ref2, null);
    }
    /**
     * Performs the matrix multiplication of ref1 on ref2, placing the result in
     * the provided target reference frame.
     * @param ref1 The inner reference frame
     * @param ref2 The second reference frame
     * @param target The target reference frame
     * @return The final reference frame
     */
    public static HDRef mult(HDRef ref1, HDRef ref2, HDRef target) {
        double[] d = new double[16];
        d[ 0] = ref1.dat[0] * ref2.dat[0]  + ref1.dat[4] * ref2.dat[1]  + ref1.dat[8]  * ref2.dat[2];
        d[ 1] = ref1.dat[1] * ref2.dat[0]  + ref1.dat[5] * ref2.dat[1]  + ref1.dat[9]  * ref2.dat[2];
        d[ 2] = ref1.dat[2] * ref2.dat[0]  + ref1.dat[6] * ref2.dat[1]  + ref1.dat[10] * ref2.dat[2];
        d[ 4] = ref1.dat[0] * ref2.dat[4]  + ref1.dat[4] * ref2.dat[5]  + ref1.dat[8]  * ref2.dat[6];
        d[ 5] = ref1.dat[1] * ref2.dat[4]  + ref1.dat[5] * ref2.dat[5]  + ref1.dat[9]  * ref2.dat[6];
        d[ 6] = ref1.dat[2] * ref2.dat[4]  + ref1.dat[6] * ref2.dat[5]  + ref1.dat[10] * ref2.dat[6];
        d[ 8] = ref1.dat[0] * ref2.dat[8]  + ref1.dat[4] * ref2.dat[9]  + ref1.dat[8]  * ref2.dat[10];
        d[ 9] = ref1.dat[1] * ref2.dat[8]  + ref1.dat[5] * ref2.dat[9]  + ref1.dat[9]  * ref2.dat[10];
        d[10] = ref1.dat[2] * ref2.dat[8]  + ref1.dat[6] * ref2.dat[9]  + ref1.dat[10] * ref2.dat[10];
        d[12] = ref1.dat[0] * ref2.dat[12] + ref1.dat[4] * ref2.dat[13] + ref1.dat[8]  * ref2.dat[14] + ref1.dat[12];
        d[13] = ref1.dat[1] * ref2.dat[12] + ref1.dat[5] * ref2.dat[13] + ref1.dat[9]  * ref2.dat[14] + ref1.dat[13];
        d[14] = ref1.dat[2] * ref2.dat[12] + ref1.dat[6] * ref2.dat[13] + ref1.dat[10] * ref2.dat[14] + ref1.dat[14];
        d[15] = 1.0f;
        return target != null ? target.set(d) : new HDRef(d);
    }
    
    // The Local Methods
    /**
     * Creates a new reference frame which performs no transformation.
     */
    public HDRef() { identity(); }
    /**
     * Creates a new reference frame translated by the given vector.
     * @param vec The translation vector
     */
    public HDRef(HDVec vec) { set(vec); }
    /**
     * Creates a new reference frame rotated by the given angle
     * @param ang The rotation angle
     */
    public HDRef(HDAng ang) { set(ang); }
    /**
     * Creates a new reference frame, such that the three provided vectors form
     * the X, Y, Z basis for the space.
     * The AB direction is the z-axis, the cross product of AB and AC form the
     * y-axis, and the cross product of the y-axis and z-axis form the x-axis.
     * @param vecA Position Vector A
     * @param vecB Position Vector B
     * @param vecC Position Vector C
     */
    public HDRef(HDVec vecA, HDVec vecB, HDVec vecC) { set(vecA, vecB, vecC); }
    /**
     * Creates a new reference frame depending on the number of vectors provided.
     * If one is provided, the new reference frame is translated by the given
     * vector.
     * If three are provided, the vectors form the X, Y, Z basis for the space.
     * Otherwise, an <code>IllegalArgumentException</code> is thrown.
     * @param vecs The vectors
     */
    public HDRef(HDVec[] vecs) { set(vecs); }
    /**
     * Creates a new reference frame with the provided column-major 4x4 matrix
     * given as a 16 double array.
     * The matrix given should be a special orthogonal affine matrix.
     * @param matrix The matrix.
     */
    public HDRef(double[] matrix) { set(matrix); }
    /**
     * Creates a new reference frame which is a duplicate of the provided frame.
     * @param ref The frame to copy
     */
    public HDRef(HDRef ref) { set(ref); }
    private final double[] dat = new double[16];
    private final DoubleBuffer buffer = ByteBuffer.allocateDirect(16 * Double.BYTES)
            .order(ByteOrder.nativeOrder()).asDoubleBuffer();
    private final DoubleBuffer read_only = buffer.asReadOnlyBuffer();
    private volatile boolean modified = true;
    
    /**
     * Clears the reference frame and sets it to a translation by the given
     * vector, then returns this.
     * @param vec The translation vector
     * @return This
     */
    public HDRef set(HDVec vec) {
        identity();
        dat[12] = vec.X(); dat[13] = vec.Y(); dat[14] = vec.Z();
        return this;
    }
    /**
     * Clears the reference frame and sets it to a rotation by the given angle,
     * then returns this.
     * @param ang The rotation angle
     * @return This
     */
    public HDRef set(HDAng ang) {
        zero();
        dat[ 0] = ang.cos() + ang.X() * ang.X() * (1 - ang.cos());
        dat[ 1] = ang.Y() * ang.X() * (1 - ang.cos()) + ang.Z() * ang.sin();
        dat[ 2] = ang.X() * ang.Z() * (1 - ang.cos()) - ang.Y() * ang.sin();
        
        dat[ 4] = ang.X() * ang.Y() * (1 - ang.cos()) - ang.Z() * ang.sin();
        dat[ 5] = ang.cos() + ang.Y() * ang.Y() * (1 - ang.cos());
        dat[ 6] = ang.Y() * ang.Z() * (1 - ang.cos()) + ang.X() * ang.sin();
        
        dat[ 8] = ang.X() * ang.Z() * (1 - ang.cos()) + ang.Y() * ang.sin();
        dat[ 9] = ang.Y() * ang.Z() * (1 - ang.cos()) - ang.X() * ang.sin();
        dat[10] = ang.cos() + ang.Z() * ang.Z() * (1 - ang.cos());
        
        dat[15] = 1.0f;
        modified = true;
        return this;
    }
    /**
     * Clears the reference frame and sets it to the basis provided by the
     * three vectors.
     * The AB direction is the z-axis, the cross product of AB and AC form the
     * y-axis, and the cross product of the y-axis and z-axis form the x-axis.
     * @param vecA Position Vector A
     * @param vecB Position Vector B
     * @param vecC Position Vector C
     * @return This
     */
    public HDRef set(HDVec vecA, HDVec vecB, HDVec vecC) {
        HDVec z = new HDVec(vecB).negTranslate(vecA);
        HDVec y = z.cross(new HDVec(vecC).negTranslate(vecA));
        HDVec x = y.cross(z).normalise();
        y.normalise(); z.normalise();
        
        set(new double[]{
            x.X(), x.Y(), x.Z(), 0.0f,
            y.X(), y.Y(), y.Z(), 0.0f,
            z.X(), z.Y(), z.Z(), 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        });
        modified = true;
        return this;
    }
    /**
     * Clears the reference frame and sets it depending on the number of
     * vectors provided, then returns this.
     * If one is provided, the new reference frame is translated by the given
     * vector.
     * If three are provided, the vectors form the X, Y, Z basis for the space.
     * Otherwise, an <code>IllegalArgumentException</code> is thrown.
     * @param vecs The vectors
     * @return This
     */
    public HDRef set(HDVec[] vecs) {
        switch (vecs.length) {
            case 1:
                return set(vecs[0]);
            case 3:
                return set(vecs[0], vecs[1], vecs[2]);
            default:
                throw new IllegalArgumentException("Array must be of length 1 or 3! Found " + vecs.length + ".");
        }
    }
    /**
     * Sets the reference frame to be described by the provided column-major
     * 4x4 matrix given as a 16 double array.
     * The matrix given should be a special orthogonal affine matrix.
     * @param matrix The matrix
     * @return This 
     */
    public HDRef set(double[] matrix) {
        if(matrix.length == dat.length) {
            System.arraycopy(matrix, 0, dat, 0, 16); modified = true;
        } else { throw new IllegalArgumentException("Array must be of length 16! Found " + matrix.length + "."); }
        return this;
    }
    /**
     * Sets the reference frame to be a copy of the provided reference frame.
     * @param ref The frame to copy
     * @return This
     */
    public HDRef set(HDRef ref) {
        return set(ref.dat);
    }
    
    /**
     * Sets this to the identity reference frame, then returns this.
     * @return This
     */
    public HDRef identity() { return set(IDENTITY); }
    /**
     * Sets this to the zero reference frame, then returns this.
     * @return This
     */
    private HDRef zero() { return set(ZERO); }
    /**
     * Sets this to its inverse. Note this expolits the fact this matrix is
     * a special orthogonal affine matrix, and so does not transform arbitrary
     * matrix arguments.
     * @return This
     */
    public HDRef invert() {
        double[] d = new double[16];
        d[0] = dat[0]; d[1] = dat[4]; d[2] = dat[8];
        d[4] = dat[1]; d[5] = dat[5]; d[6] = dat[9];
        d[8] = dat[2]; d[9] = dat[6]; d[10] = dat[10];
        d[12] = - (dat[0] * dat[12] + dat[1] * dat[13] + dat[2] * dat[14]);
        d[13] = - (dat[4] * dat[12] + dat[5] * dat[13] + dat[6] * dat[14]);
        d[14] = - (dat[8] * dat[12] + dat[9] * dat[13] + dat[10] * dat[14]);
        d[15] = 1.0f;
        return set(d);
    }
    
    /**
     * Translates the reference frame locally, that is after the transformation
     * described by this frame, then returns this.
     * It is analogous to moving an object within this reference frame.
     * @param vec The translation vector
     * @return This
     */
    public HDRef translateLocally(HDVec vec) {
        dat[12] += vec.X();
        dat[13] += vec.Y();
        dat[14] += vec.Z();
        modified = true;
        return this;
    }
    /**
     * Translates the reference frame globally, that is before the transformation
     * described by this frame, then returns this.
     * It is analogous to moving an object outside of this reference frame.
     * @param vec The translation vector
     * @return This
     */
    public HDRef translateGlobally(HDVec vec) {
        dat[12] += dat[0] * vec.X() + dat[4] * vec.Y() + dat[8] * vec.Z();
        dat[13] += dat[1] * vec.X() + dat[5] * vec.Y() + dat[9] * vec.Z();
        dat[14] += dat[2] * vec.X() + dat[6] * vec.Y() + dat[10] * vec.Z();
        modified = true;
        return this;
    }
    /**
     * Rotates the reference frame locally, that is after the transformation
     * described by this frame, then returns this.
     * It is analogous to moving an object within this reference frame.
     * @param ang The rotation angle
     * @return This
     */
    public HDRef rotateLocally(HDAng ang) {
        double[] r = new double[9], d = new double[16];
        r[0] = ang.cos() + ang.X() * ang.X() * (1 - ang.cos());
        r[1] = ang.Y() * ang.X() * (1 - ang.cos()) + ang.Z() * ang.sin();
        r[2] = ang.X() * ang.Z() * (1 - ang.cos()) - ang.Y() * ang.sin();
        r[3] = ang.X() * ang.Y() * (1 - ang.cos()) - ang.Z() * ang.sin();
        r[4] = ang.cos() + ang.Y() * ang.Y() * (1 - ang.cos());
        r[5] = ang.Y() * ang.Z() * (1 - ang.cos()) + ang.X() * ang.sin();
        r[6] = ang.X() * ang.Z() * (1 - ang.cos()) + ang.Y() * ang.sin();
        r[7] = ang.Y() * ang.Z() * (1 - ang.cos()) - ang.X() * ang.sin();
        r[8] = ang.cos() + ang.Z() * ang.Z() * (1 - ang.cos());
        
        d[ 0] = r[0] * dat[0] + r[3] * dat[1] + r[6] * dat[2];
        d[ 1] = r[1] * dat[0] + r[4] * dat[1] + r[7] * dat[2];
        d[ 2] = r[2] * dat[0] + r[5] * dat[1] + r[8] * dat[2];
        d[ 4] = r[0] * dat[4] + r[3] * dat[5] + r[6] * dat[6];
        d[ 5] = r[1] * dat[4] + r[4] * dat[5] + r[7] * dat[6];
        d[ 6] = r[2] * dat[4] + r[5] * dat[5] + r[8] * dat[6];
        d[ 8] = r[0] * dat[8] + r[3] * dat[9] + r[6] * dat[10];
        d[ 9] = r[1] * dat[8] + r[4] * dat[9] + r[7] * dat[10];
        d[10] = r[2] * dat[8] + r[5] * dat[9] + r[8] * dat[10];
        d[12] = r[0] * dat[12] + r[3] * dat[13] + r[6] * dat[14];
        d[13] = r[1] * dat[12] + r[4] * dat[13] + r[7] * dat[14];
        d[14] = r[2] * dat[12] + r[5] * dat[13] + r[8] * dat[14];
        d[15] = 1.0f;
        
        return set(d);
    }
    /**
     * Rotates the reference frame globally, that is before the transformation
     * described by this frame, then returns this.
     * It is analogous to moving an object outside of this reference frame.
     * @param vec The translation vector
     * @return This
     */
    public HDRef rotateGlobally(HDAng ang) {
        double[] r = new double[9], d = new double[16];
        r[0] = ang.cos() + ang.X() * ang.X() * (1 - ang.cos());
        r[1] = ang.Y() * ang.X() * (1 - ang.cos()) + ang.Z() * ang.sin();
        r[2] = ang.X() * ang.Z() * (1 - ang.cos()) - ang.Y() * ang.sin();
        r[3] = ang.X() * ang.Y() * (1 - ang.cos()) - ang.Z() * ang.sin();
        r[4] = ang.cos() + ang.Y() * ang.Y() * (1 - ang.cos());
        r[5] = ang.Y() * ang.Z() * (1 - ang.cos()) + ang.X() * ang.sin();
        r[6] = ang.X() * ang.Z() * (1 - ang.cos()) + ang.Y() * ang.sin();
        r[7] = ang.Y() * ang.Z() * (1 - ang.cos()) - ang.X() * ang.sin();
        r[8] = ang.cos() + ang.Z() * ang.Z() * (1 - ang.cos());
        
        d[0] = dat[0] * r[0] + dat[4] * r[1] + dat[8] * r[2] + dat[12];
        d[1] = dat[1] * r[0] + dat[5] * r[1] + dat[9] * r[2] + dat[13];
        d[2] = dat[2] * r[0] + dat[6] * r[1] + dat[10] * r[2] + dat[14];
        d[4] = dat[0] * r[3] + dat[4] * r[4] + dat[8] * r[5] + dat[12];
        d[5] = dat[1] * r[3] + dat[5] * r[4] + dat[9] * r[5] + dat[13];
        d[6] = dat[2] * r[3] + dat[6] * r[4] + dat[10] * r[5] + dat[14];
        d[8] = dat[0] * r[6] + dat[4] * r[7] + dat[8] * r[8] + dat[12];
        d[9] = dat[1] * r[6] + dat[5] * r[7] + dat[9] * r[8] + dat[13];
        d[10] = dat[2] * r[6] + dat[6] * r[7] + dat[10] * r[8] + dat[14];
        d[12] = dat[0] * r[6] + dat[4] * r[7] + dat[8] * r[8] + dat[12];
        d[13] = dat[1] * r[6] + dat[5] * r[7] + dat[9] * r[8] + dat[13];
        d[14] = dat[2] * r[6] + dat[6] * r[7] + dat[10] * r[8] + dat[14];
        d[15] = 1.0f;
        
        return set(d);
    }
    /**
     * 
     * Transforms this reference frame locally with the provided reference frame
     * transformation, that is after the transformation described by this frame,
     * then returns this.
     * It is analogous to moving an object within this reference frame.
     * @param ref The transformation frame
     * @return This
     */
    public HDRef multiplyLocally(HDRef ref) { return mult(ref, this, this); }
    /**
     * Transforms this reference frame globally with the provided reference frame
     * transformation, that is before the transformation described by this frame,
     * then returns this.
     * It is analogous to moving an object outside of this reference frame.
     * @param ref The transformation frame
     * @return This
     */
    public HDRef multiplyGlobally(HDRef ref) { return mult(this, ref, this); }
    
    /**
     * Returns the position vector transformed to inside this reference frame.
     * This is equivalent to calling <code>transform(vec, true)<\code>.
     * @param vec The global position vector
     * @return The local position vector
     */
    public HDVec transform(HDVec vec) {
        return transform(vec, true);
    }
    /**
     * Returns the vector transformed to inside this reference frame.
     * The position flag should be true if the vector is a position vector.
     * @param vec The global vector
     * @param position True if provided vector is a position vector
     * @return The local vector
     */
    public HDVec transform(HDVec vec, boolean position) {
        return new HDVec(
            dat[0] * vec.X() + dat[4] * vec.Y() + dat[8] * vec.Z() + (position ? dat[12] : 0f),
            dat[1] * vec.X() + dat[5] * vec.Y() + dat[9] * vec.Z() + (position ? dat[13] : 0f),
            dat[2] * vec.X() + dat[6] * vec.Y() + dat[10] * vec.Z() + (position ? dat[14] : 0f)
        );
    }
    /**
     * Returns the angle transformed to inside this reference frame.
     * @param ang The global angle
     * @return The local angle
     */
    public HDAng transform(HDAng ang) {
        return ang.set(transform(ang.vec(), false));
    }
    /**
     * Returns the global origin in the reference frame.
     * @return The local position of the global origin
     */
    public HDVec getOrigin() {
        return new HDVec(dat[12], dat[13], dat[14]);
    }
    
    /**
     * Returns a copy of this reference frame in a <code>LDRef</code> object.
     * @return A copy of this.
     */
    public LDRef toLD() {
        float[] d = new float[16];
        for(int i = 0; i < dat.length; i++) {
            d[i] = (float) dat[i];
        }
        return new LDRef(d);
    }
    /**
     * Returns a copy of this reference frame.
     * @return A <code>HDRef</code> copy of this.
     */
    public HDRef toHD() {
        return new HDRef(this);
    }
    
    /**
     * Returns a read-only version of the matrix in a double-buffer.
     * @return The matrix as a read-only buffer
     */
    public DoubleBuffer buffer() {
        if (modified) {
            buffer.put(dat);
            buffer.flip();
            modified = false;
        }
        return this.read_only;
    }
    /**
     * Writes the 16 double matrix in major-column format into the byte buffer,
     * then returns the provided buffer.
     * @param bb The Buffer to write to
     * @return The Buffer written to
     */
    public ByteBuffer write(ByteBuffer bb) {
        bb.putDouble(dat[ 0]).putDouble(dat[ 1]).putDouble(dat[ 2]).putDouble(dat[ 3])
          .putDouble(dat[ 4]).putDouble(dat[ 5]).putDouble(dat[ 6]).putDouble(dat[ 7])
          .putDouble(dat[ 8]).putDouble(dat[ 9]).putDouble(dat[10]).putDouble(dat[11])
          .putDouble(dat[12]).putDouble(dat[13]).putDouble(dat[14]).putDouble(dat[15]);
        return bb;
    }
    
    /**
     * Returns the transformation matrix of this reference frame in the format
     * "{{0,4,8,12}\\n{1,5,9,13}\\n{2,6,10,14}\\n{3,7,11,15}}".
     * @return The transformation matrix in text.
     */
    @Override
    public String toString() {
        return "{{" + dat[0] + "," + dat[4] + "," + dat[8] + "," + dat[12] + "}\n"
                + " {" + dat[1] + "," + dat[5] + "," + dat[9] + "," + dat[13] + "}\n"
                + " {" + dat[2] + "," + dat[6] + "," + dat[10] + "," + dat[14] + "}\n"
                + " {" + dat[3] + "," + dat[7] + "," + dat[11] + "," + dat[15] + "}}";
    }
    /**
     * Returns true only if the provided object is a <code>HDRef</code> object
     * with the same transformation matrix as this.
     * @param obj The object check
     * @return If it is equivalent
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof HDRef && Arrays.equals(dat, ((HDRef) obj).dat);
    }
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Arrays.hashCode(this.dat);
        return hash;
    }
}
