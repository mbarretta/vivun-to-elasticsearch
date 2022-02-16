package barretta.elastic.vivun.objects

import java.security.MessageDigest

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

    String fingerprint(fields) {
        MessageDigest md = MessageDigest.getInstance("MD5")
        fields.each {
            md.update(new byte[]{0})
            md.update((it ?: "").bytes)
        }
        return Base64.encoder.encodeToString(md.digest())
    }
    boolean isNew
    Map toMap
}
