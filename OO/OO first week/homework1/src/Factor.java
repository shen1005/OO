import java.math.BigInteger;

public class Factor {
    private BigInteger ratio;
    private BigInteger index;

    public BigInteger getRatio() {
        return ratio;
    }

    public void setRatio(BigInteger ratio) {
        this.ratio = ratio;
    }

    public BigInteger getIndex() {
        return index;
    }

    public void setIndex(BigInteger index) {
        this.index = index;
    }

    public void simAdd(Factor factor) {
        this.setRatio(this.getRatio().add(factor.getRatio()));
    }

    public String getDes() {
        if (this.index.equals(new BigInteger("0"))) {
            return this.ratio + "";
        }
        if (this.index.equals(new BigInteger("1")) && !this.ratio.equals(new BigInteger("1")) &&
                !this.ratio.equals(new BigInteger("-1"))) {
            return this.ratio + "*x";
        }
        if (this.index.equals(new BigInteger("1")) && this.ratio.equals(new BigInteger("1"))) {
            return "x";
        }
        if (this.index.equals(new BigInteger("1")) && this.ratio.equals(new BigInteger("-1"))) {
            return "-x";
        }
        if (this.ratio.equals(new BigInteger("1"))) {
            return "x**" + this.index;
        }
        if (this.ratio.equals(new BigInteger("-1"))) {
            return "-x**" + this.index;
        }
        return this.ratio + "*x" + "**" + this.index;
    }
}
