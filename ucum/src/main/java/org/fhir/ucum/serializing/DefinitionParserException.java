package org.fhir.ucum.serializing;

public class DefinitionParserException extends Exception {
    protected Throwable detail;
    protected int row = -1;
    protected int column = -1;

    public DefinitionParserException(String s) {
        super(s);
    }

    public DefinitionParserException(String s, Throwable thrwble) {
        super(s);
        this.detail = thrwble;
    }

    public DefinitionParserException(String s, int row, int column, Throwable detail) {
        super(s);
        this.row = row;
        this.column = column;
        this.detail = detail;
    }

    public Throwable getDetail() {
        return detail;
    }

    public int getLineNumber() {
        return row;
    }

    public int getColumnNumber() {
        return column;
    }

    //NOTE: code that prints this and detail is difficult in J2ME
    public void printStackTrace() {
        if (detail == null) {
            super.printStackTrace();
        } else {
            synchronized (System.err) {
                System.err.println(super.getMessage() + "; nested exception is:");
                detail.printStackTrace();
            }
        }
    }

}
