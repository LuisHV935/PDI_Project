package com.example.pdiproject.Algortitmos;

import java.awt.image.BufferedImage;

public class ConversionesColor {

    static BufferedImage copy(BufferedImage src) {
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        dst.getGraphics().drawImage(src, 0, 0, null);
        return dst;
    }

    static int clamp(double v) {
        return (int) Math.max(0, Math.min(255, Math.round(v)));
    }

    static int packRGB(int r, int g, int b) {
        return (255 << 24) | (r << 16) | (g << 8) | b;
    }

    public static BufferedImage canalAGris(double[] canal, int width, int height) {
        BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int v = clamp(canal[y * width + x] * 255.0);
                dst.setRGB(x, y, packRGB(v, v, v));
            }
        }
        return dst;
    }

    public static BufferedImage canalAGrisNormalizado(double[] canal, int width, int height) {
        double min = Double.MAX_VALUE, max = -Double.MAX_VALUE;
        for (double v : canal) { if (v < min) min = v; if (v > max) max = v; }
        double rango = (max - min) == 0 ? 1 : max - min;
        BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int v = clamp(((canal[y * width + x] - min) / rango) * 255.0);
                dst.setRGB(x, y, packRGB(v, v, v));
            }
        }
        return dst;
    }

    public static double[][] rgbACmy(BufferedImage src) {
        int w = src.getWidth(), h = src.getHeight(), n = w * h;
        double[] C = new double[n], M = new double[n], Y = new double[n];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = src.getRGB(x, y);
                double r = ((rgb >> 16) & 0xFF) / 255.0;
                double g = ((rgb >> 8)  & 0xFF) / 255.0;
                double b = ( rgb        & 0xFF) / 255.0;
                int idx = y * w + x;
                C[idx] = 1.0 - r;
                M[idx] = 1.0 - g;
                Y[idx] = 1.0 - b;
            }
        }
        return new double[][]{ C, M, Y };
    }

    public static BufferedImage cmyARgb(double[] C, double[] M, double[] Y, int width, int height) {
        BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int idx = y * width + x;
                int r = clamp((1.0 - C[idx]) * 255.0);
                int g = clamp((1.0 - M[idx]) * 255.0);
                int b = clamp((1.0 - Y[idx]) * 255.0);
                dst.setRGB(x, y, packRGB(r, g, b));
            }
        }
        return dst;
    }

    // Canal C en color cian, M en magenta, Y en amarillo
    public static BufferedImage canalCmyEnColor(double[] canal, int width, int height, int indice) {
        BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int v = clamp(canal[y * width + x] * 255.0);
                int packed = switch (indice) {
                    case 0 -> packRGB(0, v, v);       // Cian
                    case 1 -> packRGB(v, 0, v);       // Magenta
                    case 2 -> packRGB(v, v, 0);       // Amarillo
                    default -> packRGB(v, v, v);
                };
                dst.setRGB(x, y, packed);
            }
        }
        return dst;
    }

    public static double[][] cmyACmyk(double[] C, double[] M, double[] Y) {
        int n = C.length;
        double[] Cp = new double[n], Mp = new double[n], Yp = new double[n], K = new double[n];
        for (int i = 0; i < n; i++) {
            double k = Math.min(C[i], Math.min(M[i], Y[i]));
            K[i] = k;
            if (k >= 1.0) {
                Cp[i] = Mp[i] = Yp[i] = 0;
            } else {
                Cp[i] = (C[i] - k) / (1.0 - k);
                Mp[i] = (M[i] - k) / (1.0 - k);
                Yp[i] = (Y[i] - k) / (1.0 - k);
            }
        }
        return new double[][]{ Cp, Mp, Yp, K };
    }

    public static double[][] cmykACmy(double[] Cp, double[] Mp, double[] Yp, double[] K) {
        int n = K.length;
        double[] C = new double[n], M = new double[n], Y = new double[n];
        for (int i = 0; i < n; i++) {
            C[i] = Cp[i] * (1.0 - K[i]) + K[i];
            M[i] = Mp[i] * (1.0 - K[i]) + K[i];
            Y[i] = Yp[i] * (1.0 - K[i]) + K[i];
        }
        return new double[][]{ C, M, Y };
    }

    public static BufferedImage canalCmykEnColor(double[] canal, int width, int height, int indice) {
        BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int v = clamp(canal[y * width + x] * 255.0);
                int packed = switch (indice) {
                    case 0 -> packRGB(0, v, v);       // Cian
                    case 1 -> packRGB(v, 0, v);       // Magenta
                    case 2 -> packRGB(v, v, 0);       // Amarillo
                    case 3 -> packRGB(v, v, v);       // K (negro -> gris)
                    default -> packRGB(v, v, v);
                };
                dst.setRGB(x, y, packed);
            }
        }
        return dst;
    }

    public static double[][] rgbAYiq(BufferedImage src) {
        int w = src.getWidth(), h = src.getHeight(), n = w * h;
        double[] Y = new double[n], I = new double[n], Q = new double[n];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = src.getRGB(x, y);
                double r = ((rgb >> 16) & 0xFF) / 255.0;
                double g = ((rgb >> 8)  & 0xFF) / 255.0;
                double b = ( rgb & 0xFF) / 255.0;
                int idx = y * w + x;
                Y[idx] =  0.299 * r + 0.587 * g + 0.114 * b;
                I[idx] =  0.596 * r - 0.274 * g - 0.322 * b;
                Q[idx] =  0.211 * r - 0.523 * g + 0.312 * b;
            }
        }
        return new double[][]{ Y, I, Q };
    }

    public static BufferedImage yiqARgb(double[] Y, double[] I, double[] Q, int width, int height) {
        BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int idx = y * width + x;
                int r = clamp((Y[idx] + 0.956 * I[idx] + 0.621 * Q[idx]) * 255.0);
                int g = clamp((Y[idx] - 0.272 * I[idx] - 0.647 * Q[idx]) * 255.0);
                int b = clamp((Y[idx] - 1.106 * I[idx] + 1.703 * Q[idx]) * 255.0);
                dst.setRGB(x, y, packRGB(r, g, b));
            }
        }
        return dst;
    }

    public static BufferedImage canalYiqEnColor(double[] canal, int width, int height, int indice) {
        return switch (indice) {
            case 0 -> canalAGris(canal, width, height);
            case 1 -> canalAGrisNormalizado(canal, width, height);
            case 2 -> canalAGrisNormalizado(canal, width, height);
            default -> canalAGris(canal, width, height);
        };
    }

    public static double[][] rgbAHsi(BufferedImage src) {
        int w = src.getWidth(), h = src.getHeight(), n = w * h;
        double[] H = new double[n], S = new double[n], I = new double[n];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = src.getRGB(x, y);
                double r = ((rgb >> 16) & 0xFF) / 255.0;
                double g = ((rgb >> 8)  & 0xFF) / 255.0;
                double b = ( rgb        & 0xFF) / 255.0;
                int idx = y * w + x;

                I[idx] = (r + g + b) / 3.0;

                double minVal = Math.min(r, Math.min(g, b));
                double suma = r + g + b;
                S[idx] = (suma == 0) ? 0 : 1.0 - (3.0 * minVal / suma);

                double num = 0.5 * ((r - g) + (r - b));
                double den = Math.sqrt((r - g) * (r - g) + (r - b) * (g - b));
                double theta = (den == 0) ? 0 : Math.acos(Math.max(-1, Math.min(1, num / den)));

                H[idx] = (b <= g) ? Math.toDegrees(theta) : 360.0 - Math.toDegrees(theta);
            }
        }
        return new double[][]{ H, S, I };
    }

    public static BufferedImage hsiARgb(double[] H, double[] S, double[] I, int width, int height) {
        BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int idx = y * width + x;
                double h = H[idx], s = S[idx], intensity = I[idx];
                double r, g, b;

                if (s == 0) {
                    r = g = b = intensity;
                } else if (h < 120.0) {
                    double hRad = Math.toRadians(h);
                    b = intensity * (1.0 - s);
                    r = intensity * (1.0 + s * Math.cos(hRad) / Math.cos(Math.toRadians(60.0) - hRad));
                    g = 3.0 * intensity - (r + b);
                } else if (h < 240.0) {
                    double hp = h - 120.0;
                    double hRad = Math.toRadians(hp);
                    r = intensity * (1.0 - s);
                    g = intensity * (1.0 + s * Math.cos(hRad) / Math.cos(Math.toRadians(60.0) - hRad));
                    b = 3.0 * intensity - (r + g);
                } else {
                    double hp = h - 240.0;
                    double hRad = Math.toRadians(hp);
                    g = intensity * (1.0 - s);
                    b = intensity * (1.0 + s * Math.cos(hRad) / Math.cos(Math.toRadians(60.0) - hRad));
                    r = 3.0 * intensity - (g + b);
                }

                dst.setRGB(x, y, packRGB(clamp(r * 255), clamp(g * 255), clamp(b * 255)));
            }
        }
        return dst;
    }

    public static BufferedImage canalHsiEnColor(double[] canal, int width, int height, int indice) {
        return switch (indice) {
            case 0 -> canalAGrisNormalizado(canal, width, height);
            case 1 -> canalAGris(canal, width, height);
            case 2 -> canalAGris(canal, width, height);
            default -> canalAGris(canal, width, height);
        };
    }

    public static double[][] rgbAHsv(BufferedImage src) {
        int w = src.getWidth(), h = src.getHeight(), n = w * h;
        double[] H = new double[n], S = new double[n], V = new double[n];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = src.getRGB(x, y);
                double r = ((rgb >> 16) & 0xFF) / 255.0;
                double g = ((rgb >> 8)  & 0xFF) / 255.0;
                double b = ( rgb        & 0xFF) / 255.0;
                int idx = y * w + x;

                double cmax = Math.max(r, Math.max(g, b));
                double cmin = Math.min(r, Math.min(g, b));
                double delta = cmax - cmin;

                V[idx] = cmax;
                S[idx] = (cmax == 0) ? 0 : delta / cmax;

                if (delta == 0) {
                    H[idx] = 0;
                } else if (cmax == r) {
                    H[idx] = 60.0 * (((g - b) / delta) % 6);
                    if (H[idx] < 0) H[idx] += 360;
                } else if (cmax == g) {
                    H[idx] = 60.0 * ((b - r) / delta + 2.0);
                } else {
                    H[idx] = 60.0 * ((r - g) / delta + 4.0);
                }
            }
        }
        return new double[][]{ H, S, V };
    }

    public static BufferedImage hsvARgb(double[] H, double[] S, double[] V, int width, int height) {
        BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int idx = y * width + x;
                double h = H[idx], s = S[idx], v = V[idx];
                double C = v * s;
                double X = C * (1.0 - Math.abs((h / 60.0) % 2 - 1.0));
                double m = v - C;
                double r1, g1, b1;

                if      (h < 60)  { r1 = C; g1 = X; b1 = 0; }
                else if (h < 120) { r1 = X; g1 = C; b1 = 0; }
                else if (h < 180) { r1 = 0; g1 = C; b1 = X; }
                else if (h < 240) { r1 = 0; g1 = X; b1 = C; }
                else if (h < 300) { r1 = X; g1 = 0; b1 = C; }
                else              { r1 = C; g1 = 0; b1 = X; }

                dst.setRGB(x, y, packRGB(clamp((r1 + m) * 255), clamp((g1 + m) * 255), clamp((b1 + m) * 255)));
            }
        }
        return dst;
    }

    private static double[] rgbAXyz(double r, double g, double b) {
        double X = 0.5141 * r + 0.3239 * g + 0.1604 * b;
        double Y = 0.2651 * r + 0.6702 * g + 0.0641 * b;
        double Z = 0.0241 * r + 0.1228 * g + 0.8444 * b;
        return new double[]{ X, Y, Z };
    }

    private static double[] xyzALms(double X, double Y, double Z) {
        double L =  0.3897 * X + 0.6890 * Y - 0.0787 * Z;
        double M = -0.2298 * X + 1.1834 * Y + 0.0464 * Z;
        double S =  0.0000 * X + 0.0000 * Y + 1.0000 * Z;
        return new double[]{ Math.max(L, 1e-10), Math.max(M, 1e-10), Math.max(S, 1e-10) };
    }

    private static double[] lmsALab(double L, double M, double S) {
        double lL = Math.log10(L);
        double lM = Math.log10(M);
        double lS = Math.log10(S);

        double l = (1.0 / Math.sqrt(3))  * (lL + lM + lS);
        double alpha = (1.0 / Math.sqrt(6))  * (lL + lM - 2 * lS);
        double beta  = (1.0 / Math.sqrt(2))  * (lL - lM);
        return new double[]{ l, alpha, beta };
    }

    public static double[][] rgbALab(BufferedImage src) {
        int w = src.getWidth(), h = src.getHeight(), n = w * h;
        double[] l = new double[n], alpha = new double[n], beta = new double[n];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = src.getRGB(x, y);
                double r = ((rgb >> 16) & 0xFF) / 255.0;
                double g = ((rgb >> 8)  & 0xFF) / 255.0;
                double b = ( rgb        & 0xFF) / 255.0;

                double[] xyz  = rgbAXyz(r, g, b);
                double[] lms  = xyzALms(xyz[0], xyz[1], xyz[2]);
                double[] lab  = lmsALab(lms[0], lms[1], lms[2]);

                int idx = y * w + x;
                l[idx] = lab[0]; alpha[idx] = lab[1]; beta[idx] = lab[2];
            }
        }
        return new double[][]{ l, alpha, beta };
    }

    private static double[] labALms(double l, double alpha, double beta) {
        double s3 = Math.sqrt(3) / 3.0;
        double s6 = Math.sqrt(6) / 6.0;
        double s2 = Math.sqrt(2) / 2.0;

        double lL = s3 * l + s6 * alpha + s2 * beta;
        double lM = s3 * l + s6 * alpha - s2 * beta;
        double lS = s3 * l - 2 * s6 * alpha;

        return new double[]{ Math.pow(10, lL), Math.pow(10, lM), Math.pow(10, lS) };
    }

    private static double[] lmsAXyz(double L, double M, double S) {
        double X =  1.8600 * L - 1.1295 * M + 0.2199 * S;
        double Y =  0.3612 * L + 0.6388 * M - 0.0000 * S;
        double Z =  0.0000 * L + 0.0000 * M + 1.0000 * S;
        return new double[]{ X, Y, Z };
    }

    private static double[] xyzARgb(double X, double Y, double Z) {
        double r =  2.5625 * X - 1.1661 * Y - 0.3962 * Z;
        double g = -1.0212 * X + 1.9778 * Y + 0.0434 * Z;
        double b =  0.0747 * X - 0.2571 * Y + 1.1824 * Z;
        return new double[]{ r, g, b };
    }

    public static BufferedImage labARgb(double[] l, double[] alpha, double[] beta, int width, int height) {
        BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int idx = y * width + x;
                double[] lms = labALms(l[idx], alpha[idx], beta[idx]);
                double[] xyz = lmsAXyz(lms[0], lms[1], lms[2]);
                double[] rgb = xyzARgb(xyz[0], xyz[1], xyz[2]);
                dst.setRGB(x, y, packRGB(clamp(rgb[0] * 255), clamp(rgb[1] * 255), clamp(rgb[2] * 255)));
            }
        }
        return dst;
    }
}

