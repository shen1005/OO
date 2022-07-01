import java.math.BigInteger;
import java.util.ArrayList;

public class Factor {
    private BigInteger ratio;
    private BigInteger index;
    private ArrayList<SinCos> sinCos = new ArrayList<>();

    public void setSinCos(ArrayList<SinCos> sinCos) {
        this.sinCos = sinCos;
    }

    public ArrayList<SinCos> getSinCos() {
        return sinCos;
    }

    public void addSinCos(SinCos sinCos) {
        this.sinCos.add(sinCos);
    }

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

    public ArrayList<SinCos> sinMul(Factor factor) {
        ArrayList<SinCos> sin1 = new ArrayList<>();
        for (SinCos i: this.sinCos) {
            SinCos temp = new SinCos();
            temp.setExpr(i.getExpr());
            temp.setSin(i.getIsSin());
            temp.setOutIndex(i.getOutIndex());
            sin1.add(temp);
        }
        for (SinCos i: factor.sinCos) {
            SinCos temp = new SinCos();
            temp.setExpr(i.getExpr());
            temp.setSin(i.getIsSin());
            temp.setOutIndex(i.getOutIndex());
            sin1.add(temp);
        }
        for (int i = 0; i < sin1.size(); i++) {
            for (int j = i + 1; j < sin1.size(); j++) {
                if (sin1.get(i).isSame(sin1.get(j))) {
                    sin1.get(i).mul(sin1.get(j));
                    sin1.remove(j);
                    j--;
                }
            }
        }
        return sin1;
    }

    public boolean isAllSame(Factor other) { //相同
        boolean flag1 = this.index.equals(other.index);
        boolean flag2 = this.ratio.equals(other.ratio);
        boolean flag3 = (this.sinCos.size() == 0 && other.sinCos.size() == 0) ||
                this.isSame(other);
        return flag1 && flag2 && flag3;
    }

    public boolean isSame(Factor other) { //可加
        boolean flag = this.index.equals(other.index);
        boolean tv = true;
        for (SinCos i: this.sinCos) {
            boolean tv2 = false;
            for (SinCos j: other.sinCos) {
                tv2 = tv2 || i.isEqual(j);
            }
            tv = tv && tv2;
        }
        return flag && tv && this.sinCos.size() == other.sinCos.size();
    }

    public boolean isComplement(Factor other) {
        return this.sinCos.size() == 1 && other.sinCos.size() == this.sinCos.size() &&
                this.sinCos.get(0).forCom() + other.sinCos.get(0).forCom() == 1 &&
                this.sinCos.get(0).getExpr().equals(other.sinCos.get(0).getExpr()) &&
                this.ratio.compareTo(new BigInteger("0")) *
                        other.ratio.compareTo(new BigInteger("0")) == 1 &&
                this.index.equals(other.index);

    }

    public String getDes() {
        String temp = "";
        for (SinCos i: this.sinCos) {
            temp = String.format("%s%s", temp, "*" + i.getDes());
        }
        if (this.index.equals(new BigInteger("0"))) {
            if (this.ratio.equals(new BigInteger("1")) && !temp.equals("")) {
                return temp.substring(1);
            }
            else if (this.ratio.equals(new BigInteger("-1")) && !temp.equals("")) {
                return "-" + temp.substring(1);
            }
            return this.ratio + "" + temp;
        }
        if (this.index.equals(new BigInteger("1")) && !this.ratio.equals(new BigInteger("1")) &&
                !this.ratio.equals(new BigInteger("-1"))) {
            return this.ratio + "*x" + temp;
        }
        if (this.index.equals(new BigInteger("1")) && this.ratio.equals(new BigInteger("1"))) {
            return "x" + temp;
        }
        if (this.index.equals(new BigInteger("1")) && this.ratio.equals(new BigInteger("-1"))) {
            return "-x" + temp;
        }
        if (this.ratio.equals(new BigInteger("1"))) {
            return "x**" + this.index + temp;
        }
        if (this.ratio.equals(new BigInteger("-1"))) {
            return "-x**" + this.index + temp;
        }
        return this.ratio + "*x" + "**" + this.index + temp;
    }
}