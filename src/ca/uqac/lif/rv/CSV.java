package ca.uqac.lif.rv;

public class CSV {

    public static final String PARAM_SEP = "//";


    public class Fields {
        public static final String EVENT_TIMESTAMP = "Event timestamp";
        public static final String EVENT_ID = "Event ID";
        public static final String EVENT_TYPE = "Event type";
        public static final String CALLING_OBJECT = "Calling object";
        public static final String CALLING_OBJECT_IHC = "Calling object identity hashcode";
        public static final String METHOD_RETURN_TYPE = "Method return type";
        public static final String METHOD_NAME = "Method name";
        public static final String METHOD_CALL_DEPTH = "Method call depth";
        public static final String METHOD_PARAM_NB = "Method parameters number";
        public static final String METHOD_PARAM_TYPES = "Parameters types";
        public static final String METHOD_PARAM_VALUES = "Parameters values";
    }

    public class Values {
        public static final String TYPE_CALL = "call";
        public static final String TYPE_RETURN = "return";
    }

}
