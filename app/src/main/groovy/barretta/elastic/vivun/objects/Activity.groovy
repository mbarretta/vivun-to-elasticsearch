package barretta.elastic.vivun.objects

class Activity extends VivunCsvObject {

    Activity(String[] data) {
        super.dataArray = data
    }

    def getOwner() { return dataArray[0] }
    def getAccount() { return dataArray[1] }
    def getOpportunity() { return dataArray[2] }
    def getType() { return dataArray[3] }
    def getDescription() { return dataArray[4] }
    def getData() { return dataArray[5] }
    def getHours() { return dataArray[6] as int }
    def getDeliverable() { return dataArray[7] }
    def getDeleted() { return dataArray[8] }
    def getCreatedDate() { return dataArray[9] }
    def getCreatedBy() { return dataArray[10] }
    def getLastModifiedDate() { return dataArray[11] }
    def getLastModifiedBy() { return dataArray[12] }
    def getLastViewedDate() { return dataArray[13] }
    def getLastReferencedDate() { return dataArray[14] }
    def getCreatedStage() { return dataArray[15] }
    def getDefaultHours() { return dataArray[16] }
    def getFocus() { return dataArray[17] }
    def getHeroActivityFlag() { return dataArray[18] }
    def getPresalesStage() { return dataArray[19] }
    def getSource() { return dataArray[20] }
    def getStatus() { return dataArray[21] }
    def getHeroActivityNumber() { return dataArray[22] }
}
