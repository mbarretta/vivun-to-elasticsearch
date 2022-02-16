package barretta.elastic.vivun.objects

import org.elasticsearch.search.SearchHit

class Activity extends VivunObject {

    Activity(String[] data) {
        super.dataArray = data
    }

    Activity(SearchHit hit) {
        def map = hit.sourceAsMap
        setOwner(map["Owner"])
        setAccount(map["Account"])
        setOpportunity(map["Opportunity"])
        setType(map["Type"])
        setDescription(map["Description"])
        setData(map["Data"])
        setHours(map["Hours"])
        setDeliverable(map["Deliverable"])
        setDeleted(map["Deleted"])
        setCreatedDate(map["Created Date"])
        setCreatedBy(map["Created By"])
        setLastModifiedDate(map["Last Modified Date"])
        setLastModifiedBy(map["Last Modified By"])
        setLastViewedDate(map["Last Viewed Date"])
        setLastReferencedDate(map["Last Referenced Date"])
        setCreatedStage(map["Created Stage"])
        setFocus(map["Focus"])
        setHeroActivityFlag(map["Hero Activity Flag"])
        setPresalesStage(map["Presales Stage"])
        setSource(map["Source"])
        setStatus(map["Status"])
        setHeroActivityNumber(map["Hero Activity Number"])
        setRecordID(map["Record ID"])
    }

    def getOwner() { return dataArray[0] }
    def setOwner(o) { dataArray[0] = o }
    def getAccount() { return dataArray[1] }
    def setAccount(a) { dataArray[1] = a }
    def getOpportunity() { return dataArray[2] }
    def setOpportunity(o) { dataArray[2] = o }
    def getType() { return dataArray[3] }
    def setType(t) { dataArray[3] = t }
    def getDescription() { return dataArray[4] }
    def setDescription(d) { dataArray[4] = d }
    def getData() { return dataArray[5] }
    def setData(d) { dataArray[5] = d }
    float getHours() { return (dataArray[6] ?: 0 ) as float }
    def setHours(h) { dataArray[6] = h }
    def getDeliverable() { return dataArray[7] }
    def setDeliverable(d) { dataArray[7] = d }
    def getDeleted() { return dataArray[8] }
    def setDeleted(d) { dataArray[8] = d}
    def getCreatedDate() { return dataArray[9] }
    def setCreatedDate(d) { dataArray[9] = d }
    def getCreatedBy() { return dataArray[10] }
    def setCreatedBy(b) { dataArray[10] = b }
    def getLastModifiedDate() { return dataArray[11] }
    def setLastModifiedDate(d) { dataArray[11] = d }
    def getLastModifiedBy() { return dataArray[12] }
    def setLastModifiedBy(b) { dataArray[12] = b }
    def getLastViewedDate() { return dataArray[13] }
    def setLastViewedDate(d) { dataArray[13] = d }
    def getLastReferencedDate() { return dataArray[14] }
    def setLastReferencedDate(d) { dataArray[14] = d }
    def getCreatedStage() { return dataArray[15] }
    def setCreatedStage(s) { dataArray[15] = s }
    def getDefaultHours() { return dataArray[16] }
    def getFocus() { return dataArray[17] }
    def setFocus(f) { dataArray[17] = f}
    def getHeroActivityFlag() { return dataArray[18] }
    def setHeroActivityFlag(f) { dataArray[18] = f }
    def getPresalesStage() { return dataArray[19] }
    def setPresalesStage(s) { dataArray[19] = s }
    def getSource() { return dataArray[20] }
    def setSource(s) { dataArray[20] = s }
    def getStatus() { return dataArray[21] }
    def setStatus(s) { dataArray[21] = s }
    def getHeroActivityNumber() { return dataArray[22] }
    def setHeroActivityNumber(n) { dataArray[22] = n }
    def getRecordID() { return dataArray[23] }
    def setRecordID(d) { dataArray[23] = d }

    def getOpportunityAccountHash() { return fingerprint([account, opportunity]) }
}
