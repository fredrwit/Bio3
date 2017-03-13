package bio3;

/**
 * Implementation of the Distance interface.
 * @param one first pixel
 * @param two second pixel
 * @return color difference between pixels
 */
public class PixelDistance<T> {

    /** Take the square root of the squares. */
    static final double PT5 = 0.5;

    /** Empty constructor for PixelDistance. */
    public PixelDistance() {

    }

    /** Finds the color distance between two pixels. */
    public double distance (Pixel one, Pixel two) {
        double square = Math.pow(one.r() - two.r(), 2) 
            + Math.pow(one.g() - two.g(), 2) 
            + Math.pow(one.b() - two.b(), 2);
        return Math.pow(square, PT5);
    }
}