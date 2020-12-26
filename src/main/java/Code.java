public class Code {
    private final int length;
    private final long code;

    public Code(int length, long code) {
        this.length = length;
        this.code = code;
    }

    public int getLength() {
        return length;
    }

    public long getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Code code1 = (Code) o;

        if (length != code1.length) return false;
        return code == code1.code;
    }

    @Override
    public int hashCode() {
        int result = length;
        result = 31 * result + (int) (code ^ (code >>> 32));
        return result;
    }
}
