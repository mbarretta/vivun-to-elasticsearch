package barretta.elastic.vivun.objects

class VivunObject {
    def dataArray = [""]*70 //haaaaack

    String toCsv() {
        return '"'+dataArray.join('","')+'"'
    }

    float toNum(String vivunAmt) {
        try {
            return vivunAmt == null || vivunAmt.trim().isEmpty() ? 0.0 : vivunAmt.replaceAll(/\D/, "") as float
        } catch (NumberFormatException e) {
            e.printStackTrace()
        }
    }

    float toNum(Double amt) {
        return amt.floatValue()
    }

    boolean isNew
    Map toMap
}
