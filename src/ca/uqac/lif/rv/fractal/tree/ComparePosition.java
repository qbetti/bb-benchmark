package ca.uqac.lif.rv.fractal.tree;

import ca.uqac.lif.cep.functions.BinaryFunction;

public class ComparePosition extends BinaryFunction<String[], Object, Boolean> {

    private int width;
    private int height;
    private boolean isTreeInside;


    public ComparePosition() {
        super(String[].class, Object.class, Boolean.class);
        width = -1;
        height = -1;
        isTreeInside = true;
    }

    @Override
    public Boolean getValue(String[] paramValues, Object objMethodName) {
        String methodName = (String) objMethodName;

        if (methodName.contains("FractalTree.setBounds")) {
            width = Integer.valueOf(paramValues[2]);
            height = Integer.valueOf(paramValues[3]);
        }

        else if(methodName.contains("java.awt.Graphics.drawLine")) {
            if (width == -1 || height == -1) {
                System.out.println("Bounds were not set before drawLine");
                isTreeInside = false;
            }
            else {
                int x2 = Integer.valueOf(paramValues[2]);
                int y2 = Integer.valueOf(paramValues[3]);

                if(!isInsideBounds(x2, y2)) {
                    isTreeInside = false;
                }
            }
        }

        return isTreeInside;
    }

    private boolean isInsideBounds(int x, int y) {
        return 0 <= x && x < width
                && 0 <= y && y < height;
    }
}
