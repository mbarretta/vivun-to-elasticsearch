package barretta.elastic.vivun.objects

import groovy.util.logging.Slf4j
import org.elasticsearch.search.SearchHit

import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Slf4j
class Opportunity extends VivunObject implements Comparable {
    def newUpdate = true

    Opportunity(String[] data) {
        super.dataArray = data
    }

    Opportunity(SearchHit hit) {
        def map = hit.sourceAsMap
        newUpdate = false
        setAccount(map["Account"])
        setACVUpsellAttrition(map["ACV Upsell Attrition"])
        setAmount(map["Amount"])
        setClosed(map["Closed"])
        setCloseDate(map["Close Date"])
        setContractAmount(map["Contract Amount"])
        setCreatedBy(map["Created By"])
        setCreatedDate(map["Created Date"])
        setDeleted(map["Deleted"])
        setExpansionACV(map["Expansion ACV"])
        setExpectedAmount(map["Expected Amount"])
        setFiscalPeriod(map["Fiscal Period"])
        setFiscalQuarter(map["Fiscal Quarter"])
        setFiscalYear(map["Fiscal Year"])
        setHeroScore(map["Hero Score"])
        setLastActivity(map["Last Activity"])
        setLastModifiedBy(map["Last Modified By"])
        setLastModifiedDate(map["Last Modified Date"])
        setLeadSource(map["Lead Source"])
        setNetAmount(map["Net Amount"])
        setNewAndUpsellAmount(map["New And Upsell Amount"])
        setOpportunityId(map["Opportunity ID"])
        setOpportunity(map["Opportunity"])
        setOpportunityName(map["Opportunity Name"])
        setOpportunityType(map["Opportunity Type"])
        setOwnerId(map["Owner ID"])
        setPOAmountTotal(map["PO Amount Total"])
        setPresalesStage(map["Presales Stage"])
        setProbability(map["Probability"])
        setRecordType(map["Record Type"])
        setRenewalMY(map["Renewal MY"])
        setRenewedSubscriptionAmount(map["Renewed Subscription Amount (Renewal)"])
        setTCVAmount(map["TCV Amount"])
        setTotalPartnerUpliftAmount(map["Total Partner Uplift Amount"])
        setWon(map["Won"])
    }

    //the tech debt spawned from the seemingly simple decision to just leave the data in the original CSV String[] vs building a "real" object
    //is ever growing...
    Map toMap() {
        return [
            "_id": opportunityId,
            "Opportunity Name": opportunityName,
            "Account": account,
            "Owner ID": ownerId,
            "Close Date": closeDate,
            "Stage": stage,
            "Forecast Category": forecastCategory,
            "Hero Score": heroScore,
            "Opportunity ID": opportunityId,
            "Deleted": deleted,
            "Record Type": recordType,
            "Private": getPrivate(),
            "Description": description,
            "Amount": amount,
            "Probability": probability,
            "Expected Amount": expectedAmount,
            "OpportunityType": opportunityType,
            "Next Step": nextStep,
            "Lead Source": leadSource,
            "Closed": closed,
            "Won": won,
            "OpportunityCurrency": opportunityCurrency,
            "Campaign": campaign,
            "Has Line Item": hasLineItem,
            "Is Split": isSplit,
            "Price Book": priceBook,
            "Territory": territory,
            "Exclude from the territory assignment filter logic": excludeFromTheTerritoryAssignmentFilterLogic,
            "Created Date": createdDate,
            "Created By": createdBy,
            "Last Modified Date": lastModifiedDate,
            "Last Modified By": lastModifiedBy,
            "System Modstamp": systemModstamp,
            "Last Activity": lastActivity,
            "Fiscal Quarter": fiscalQuarter,
            "Fiscal Year": fiscalYear,
            "Fiscal Period": fiscalPeriod,
            "Last Viewed Date": lastViewedDate,
            "Last Referenced Date": lastReferencedDate,
            "Has Open Activity": hasOpenActivity,
            "Has Overdue Task": hasOverdueTask,
            "Balance": balance,
            "Contract Amount": contractAmount,
            "Can Revive?": canRevive,
            "Date Revived": dateRevived,
            "ACV Upsell Attrition": getACVUpsellAttrition(),
            "TCV Amount": getTCVAmount(),
            "Greatest Opp Line Amount": greatestOppLineAmount,
            "ARR Amount": getARRAmount(),
            "Net Amount": netAmount,
            "Hero Opportunity Flag": heroOpportunityFlag,
            "Presales Concern": presalesConcern,
            "Presales Stage": presalesStage,
            "Technical Differentiation": technicalDifferentiation,
            "New And Upsell Amount": newAndUpsellAmount,
            "Base Renewal Amount": baseRenewalAmount,
            "Expansion ACV": expansionACV,
            "Renewal MY": renewalMY,
            "Renewed Subscription Amount (Renewal)": renewedSubscriptionAmount,
            "Total Partner Uplift Amount": totalPartnerUpliftAmount,
            "RM Forecast": getRMForecast(),
            "PO Amount (01)": getPOAmount01(),
            "PO Amount Total": getPOAmountTotal(),
            "My Geolocation (Latitude)": myGeolocationLatitude,
            "My Geolocation (Longitude)": myGeolocationLongitude,
            "SA Manager Comments": getSAManagerComments()
        ]
    }

    def isNew() {
        def created = LocalDate.parse(createdDate[0..9], DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        return lastModifiedDate && LocalDate.parse(lastModifiedDate[0..9], DateTimeFormatter.ofPattern("MM/dd/yyyy")).equals(created)
    }

    def getOpportunity() { return dataArray[0] }
    def setOpportunity(o) { dataArray[0] = o }
    def getOpportunityName() { return dataArray[0] }
    def setOpportunityName(name) { dataArray[0] = name}
    def getAccount() { return dataArray[1] }
    def setAccount(account) { dataArray[1] = account }
    def getOwnerId() { return dataArray[2] }
    def setOwnerId(id) { dataArray[2] = id }
    def getCloseDate() { return dataArray[3] }
    def setCloseDate(date) { dataArray[3] = date }
    def getStage() { return dataArray[4] }
    def getStage(stage) { dataArray[4] = stage }
    def getForecastCategory() { return dataArray[5] }
    def setForecastCategory(cat) { dataArray[5]  = cat}
    def getHeroScore() { return (dataArray[6] ?: 0) as float}
    def setHeroScore(score) { dataArray[6] = score }
    def getOpportunityId() { return dataArray[7] }
    def setOpportunityId(id) { dataArray[7] = id}
    def getDeleted() { return dataArray[8] }
    def setDeleted(deleted) { dataArray[8] = deleted}
    def getRecordType() { return dataArray[9] }
    def setRecordType(type) { dataArray[9] = type }
    def getPrivate() { return dataArray[10] }
    def setPrivate(pri) { dataArray[10] = pri }
    def getDescription() { return dataArray[11] }
    def setDescription(description) { dataArray[11] = description }
    def getAmount() { return toNum(dataArray[13])}
    def setAmount(amt) { dataArray[13] = amt}
    def getProbability() { return dataArray[14] }
    def setProbability(prob) { dataArray[14] = prob }
    def getExpectedAmount() { return dataArray[16] }
    def setExpectedAmount(amt) { dataArray[16] = amt }
    def getOpportunityType() { return dataArray[17] }
    def setOpportunityType(type) { dataArray[17] = type}
    def getNextStep() { return dataArray[18] }
    def setNextStep(step) { dataArray[18] = step }
    def getLeadSource() { return dataArray[19] }
    def setLeadSource(source) { dataArray[19] = source }
    def getClosed() { return dataArray[20] }
    def setClosed(closed) { dataArray[20] = closed }
    def getWon() { return dataArray[21] }
    def setWon(won) { dataArray[21] = won }
    def getOpportunityCurrency() { return dataArray[22] }
    def setOpportunityCurrency(curr) { dataArray[22] = curr }
    def getCampaign() { return dataArray[23] }
    def setCampaign(camp) { dataArray[23] = camp }
    def getHasLineItem() { return dataArray[24] }
    def setHasLineItem(i) { dataArray[24] = i }
    def getIsSplit() { return dataArray[25] }
    def setIsSplit(split) { dataArray[25] = split }
    def getPriceBook() { return dataArray[26] }
    def setPriceBook(book) { dataArray[26] = book }
    def getTerritory() { return dataArray[27] }
    def setTerritory(terr) { dataArray[27] = terr }
    def getExcludeFromTheTerritoryAssignmentFilterLogic() { return dataArray[28] }
    def setExcludeFromTheTerritoryAssignmentFilterLogic(logic) { dataArray[28] = logic }
    def getCreatedDate() { return dataArray[29] }
    def setCreatedDate(date) { dataArray[29] = date }
    def getCreatedBy() { return dataArray[30] }
    def setCreatedBy(by) { dataArray[30] = by }
    def getLastModifiedDate() { return dataArray[31] }
    def setLastModifiedDate(date) { dataArray[31] = date }
    def getLastModifiedBy() { return dataArray[32] }
    def setLastModifiedBy(by) { dataArray[32] = by }
    def getSystemModstamp() { return dataArray[33] }
    def setSystemModstamp(stamp) { dataArray[33] = stamp }
    def getLastActivity() { return dataArray[34] }
    def setLastActivity(act) { dataArray[34] = act}
    def getFiscalQuarter() { return dataArray[35] }
    def setFiscalQuarter(q) { dataArray[35] = q }
    def getFiscalYear() { return dataArray[36] }
    def setFiscalYear(y) { dataArray[36] = y }
    def getFiscalPeriod() { return dataArray[37] }
    def setFiscalPeriod(p) { dataArray[37] = p}
    def getLastViewedDate() { return dataArray[38] }
    def setLastViewedDate(d) { dataArray[38] = d }
    def getLastReferencedDate() { return dataArray[39] }
    def setLastReferencedDate(d) { dataArray[39] = d }
    def getHasOpenActivity() { return dataArray[40] }
    def setHasOpenActivity(a) { dataArray[40] = a }
    def getHasOverdueTask() { return dataArray[41] }
    def setHasOverdueTask(t) { dataArray[41] = t }
    def getBalance() { return dataArray[43] }
    def setBalance(b) { dataArray[43] = b }
    def getContractAmount() { return dataArray[45] }
    def setContractAmount(a) { dataArray[45] = a }
    def getCanRevive() { return dataArray[46] }
    def setCanRevive(r) { dataArray[46] = r }
    def getDateRevived() { return dataArray[47] }
    def setDateRevived(r) { dataArray[47] = r }
    def getACVUpsellAttrition() { return dataArray[49] }
    def setACVUpsellAttrition(a) { dataArray[49] = a }
    def getTCVAmount() { return toNum(dataArray[51]) }
    def setTCVAmount(a) { dataArray[51] = a }
    def getGreatestOppLineAmount() { return dataArray[53] }
    def setGreatestOppLineAmount(a) { dataArray[53] = a }
    def getARRAmount() { return toNum(dataArray[55]) }
    def setARRAmount(a) { dataArray[55] = a }
    def getNetAmount() { return toNum(dataArray[57]) }
    def setNetAmount(a) { dataArray[57] = a }
    def getHeroOpportunityFlag() { return dataArray[58] }
    def setHeroOpportunityFlag(f) { dataArray[58] = f }
    def getPresalesConcern() { return dataArray[59] }
    def setPresalesConcern(c) { dataArray[59] = c }
    def getPresalesStage() { return dataArray[60] }
    def setPresalesStage(s) { dataArray[60] = s }
    def getTechnicalDifferentiation() { return dataArray[61] }
    def setTechnicalDifferentiation(d) { dataArray[61] = d }
    def getNewAndUpsellAmount() { return toNum(dataArray[63]) }
    def setNewAndUpsellAmount(a) { dataArray[63] = a }
    def getBaseRenewalAmount() { return toNum(dataArray[65]) }
    def setBaseRenewalAmount(a) { dataArray[65] = a }
    def getExpansionACV() { return toNum(dataArray[67]) }
    def setExpansionACV(a) { dataArray[67] = a }
    def getRenewalMY() { return toNum(dataArray[69]) }
    def setRenewalMY(y) { dataArray[69] = y }
    def getRenewedSubscriptionAmount() { return toNum(dataArray[71]) }
    def setRenewedSubscriptionAmount(a) { dataArray[71] = a }
    def getTotalPartnerUpliftAmount() { return toNum(dataArray[73]) }
    def setTotalPartnerUpliftAmount(a) { dataArray[73] = a }
    def getRMForecast() { return dataArray[75] }
    def setRMForecast(f) { dataArray[75] = f }
    def getPOAmount01() { return dataArray[77] }
    def setPOAmount01(a) { dataArray[77] = a }
    def getPOAmountTotal() { return toNum(dataArray[78]) }
    def setPOAmountTotal(t) { dataArray[78] = t }
    def getMyGeolocationLatitude() { return dataArray[79] }
    def setMyGeolocationLatitude(l) { dataArray[79] = l}
    def getMyGeolocationLongitude() { return dataArray[80] }
    def setMyGeolocationLongitude(l) { dataArray[80] = l }
    def getSAManagerComments() { return dataArray[81] }
    def setSAManagerComments(c) { dataArray[81] = c }

    def getOpportunityAccountHash() { return fingerprint([account, opportunity]) }

    @Override
    int compareTo (Object o) {
        def oOpp = o as Opportunity
        def oDate
        def thisDate
        if (this.createdDate.contains("T")) {
            thisDate = LocalDate.parse(this.createdDate, "yyyy-MM-dd'T'HH:mm:ss.SSSX")
        } else {
            if (this.createdDate.length() == 10){
                thisDate = LocalDate.parse(this.createdDate.toUpperCase(), "MM/dd/yyyy")
            } else {
                thisDate = LocalDate.parse(this.createdDate.toUpperCase(), "MM/dd/yyyy h:mm a")
            }
        }
        if (oOpp.createdDate.contains("T")) {
            oDate = LocalDate.parse(oOpp.createdDate, "yyyy-MM-dd'T'HH:mm:ss.SSSX")
        } else {
            if (oOpp.createdDate.length() == 10){
                oDate = LocalDate.parse(oOpp.createdDate.toUpperCase(), "MM/dd/yyyy")
            } else {
                oDate = LocalDate.parse(oOpp.createdDate.toUpperCase(), "MM/dd/yyyy h:mm a")
            }
        }
        return oDate <=> thisDate
    }
}