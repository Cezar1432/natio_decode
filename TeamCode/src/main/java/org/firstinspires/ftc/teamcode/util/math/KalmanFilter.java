package org.firstinspires.ftc.teamcode.util.math;

public class KalmanFilter {

    private double x;
    private double p;
    private double q;
    private double r;

    public KalmanFilter(double x0, double p0, double q, double r) {
        this.x = x0;
        this.p = Math.max(1e-9, p0);
        this.q = Math.max(1e-9, q);
        this.r = Math.max(1e-9, r);
    }

    public void reset(double x0, double p0) {
        this.x = x0;
        this.p = Math.max(1e-9, p0);
    }

    public void setQ(double q) { this.q = Math.max(1e-9, q); }
    public void setR(double r) { this.r = Math.max(1e-9, r); }

    public double update(double z) {

        p = p + q;

        double k = p / (p + r);
        x = x + k * (z - x);
        p = (1.0 - k) * p;

        return x;
    }

    public double update(double z, double dtSeconds) {
        double dt = Math.max(1e-3, dtSeconds);

        // predict (scale q by dt)
        p = p + (q * dt);

        // update
        double k = p / (p + r);
        x = x + k * (z - x);
        p = (1.0 - k) * p;

        return x;
    }

    public void applyImpulse(double delta) {
        x += delta;
        p *= 1.10;
    }

    public double getX() { return x; }
    public double getP() { return p; }


}
