package barretta.elastic.vivun.objects

class VivunCsvObject {
    def dataArray

    def toCsv() {
        return '"'+dataArray.join('","')+'"'
    }

    def toNum(String vivunAmt) {
        return vivunAmt.isEmpty() ? 0 : vivunAmt.replaceAll(/\D/,"") as float
    }
}
