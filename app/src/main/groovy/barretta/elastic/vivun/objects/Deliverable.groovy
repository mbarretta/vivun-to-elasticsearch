package barretta.elastic.vivun.objects

class Deliverable extends VivunCsvObject {

    Deliverable(String[] data) {
        super.dataArray = data
    }

    def getDeliverableNumber() { return dataArray[0] }
    def getName() { return dataArray[1] }
    def getAccount() { return dataArray[6] }
}