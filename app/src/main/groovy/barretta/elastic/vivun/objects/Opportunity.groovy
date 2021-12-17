package barretta.elastic.vivun.objects

import org.elasticsearch.search.SearchHit

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Opportunity extends VivunObject{
    def Opportunity(String[] data) {
        super.dataArray = data
    }

    def Opportunity(SearchHit hit) {
        def map = hit.sourceAsMap
        setAccount(map["Account"])
        setACVUpsellAttrition(map["ACV Upsell Attrition"])
        setAmount(map["Amount"])
        setClosed(map["Closed"])
        setCloseDate(map["Closed Date"])
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
            "Total Discount Amount": totalDiscountAmount,
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
            "PO Amount (02)": getPOAmount02(),
            "PO Amount (03)": getPOAmount03(),
            "PO Amount (04)": getPOAmount04(),
            "PO Amount (05)": getPOAmount05(),
            "PO Amount Total": getPOAmountTotal(),
            "My Geolocation (Latitude)": myGeolocationLatitude,
            "My Geolocation (Longitude)": myGeolocationLongitude,
            "SA Manager Qualification": getSAManagerQualification(),
            "SA Manager Comments": getSAManagerComments()
        ]
    }

    def isNew() {
        def created = LocalDate.parse(createdDate[0..9], DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        return lastModifiedDate && LocalDate.parse(lastModifiedDate[0..9], DateTimeFormatter.ofPattern("MM/dd/yyyy")).equals(created)
    }

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
    def getHeroScore() { return dataArray[6] }
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
    def getAmount() { return toNum(dataArray[12])}
    def setAmount(amt) { dataArray[12] = amt}
    def getProbability() { return dataArray[13] }
    def setProbability(prob) { dataArray[13] = prob }
    def getExpectedAmount() { return dataArray[14] }
    def setExpectedAmount(amt) { dataArray[14] = amt }
    def getOpportunityType() { return dataArray[15] }
    def setOpportunityType(type) { dataArray[15] = type}
    def getNextStep() { return dataArray[16] }
    def setNextStep(step) { dataArray[16] = step }
    def getLeadSource() { return dataArray[17] }
    def setLeadSource(source) { dataArray[17] = source }
    def getClosed() { return dataArray[18] }
    def setClosed(closed) { dataArray[18] = closed }
    def getWon() { return dataArray[19] }
    def setWon(won) { dataArray[19] = won }
    def getOpportunityCurrency() { return dataArray[20] }
    def setOpportunityCurrency(curr) { dataArray[20] = curr }
    def getCampaign() { return dataArray[21] }
    def setCampaign(camp) { dataArray[21] = camp }
    def getHasLineItem() { return dataArray[22] }
    def setHasLineItem(i) { dataArray[22] = i }
    def getIsSplit() { return dataArray[23] }
    def setIsSplit(split) { dataArray[23] = split }
    def getPriceBook() { return dataArray[24] }
    def setPriceBook(book) { dataArray[24] = book }
    def getTerritory() { return dataArray[25] }
    def setTerritory(terr) { dataArray[25] = terr }
    def getExcludeFromTheTerritoryAssignmentFilterLogic() { return dataArray[26] }
    def setExcludeFromTheTerritoryAssignmentFilterLogic(logic) { dataArray[26] = logic }
    def getCreatedDate() { return dataArray[27] }
    def setCreatedDate(date) { dataArray[27] = date }
    def getCreatedBy() { return dataArray[28] }
    def setCreatedBy(by) { dataArray[28] = by }
    def getLastModifiedDate() { return dataArray[29] }
    def setLastModifiedDate(date) { dataArray[29] = date }
    def getLastModifiedBy() { return dataArray[30] }
    def setLastModifiedBy(by) { dataArray[30] = by }
    def getSystemModstamp() { return dataArray[31] }
    def setSystemModstamp(stamp) { dataArray[31] = stamp }
    def getLastActivity() { return dataArray[32] }
    def setLastActivity(act) { dataArray[32] = act}
    def getFiscalQuarter() { return dataArray[33] }
    def setFiscalQuarter(q) { dataArray[33] = q }
    def getFiscalYear() { return dataArray[34] }
    def setFiscalYear(y) { dataArray[34] = y }
    def getFiscalPeriod() { return dataArray[35] }
    def setFiscalPeriod(p) { dataArray[35] = p}
    def getLastViewedDate() { return dataArray[36] }
    def setLastViewedDate(d) { dataArray[36] = d }
    def getLastReferencedDate() { return dataArray[37] }
    def setLastReferencedDate(d) { dataArray[37] = d }
    def getHasOpenActivity() { return dataArray[38] }
    def setHasOpenActivity(a) { dataArray[38] = a }
    def getHasOverdueTask() { return dataArray[39] }
    def setHasOverdueTask(t) { dataArray[39] = t }
    def getBalance() { return dataArray[40] }
    def setBalance(b) { dataArray[40] = b }
    def getContractAmount() { return dataArray[41] }
    def setContractAmount(a) { dataArray[41] = a }
    def getCanRevive() { return dataArray[42] }
    def setCanRevive(r) { dataArray[42] = r }
    def getTotalDiscountAmount() { return toNum(dataArray[43]) }
    def setTotalDiscountAmount(a) { dataArray[43] = a }
    def getDateRevived() { return dataArray[44] }
    def setDateRevived(r) { dataArray[44] = r }
    def getACVUpsellAttrition() { return dataArray[45] }
    def setACVUpsellAttrition(a) { dataArray[45] = a }
    def getTCVAmount() { return toNum(dataArray[46]) }
    def setTCVAmount(a) { dataArray[46] = a }
    def getGreatestOppLineAmount() { return dataArray[47] }
    def setGreatestOppLineAmount(a) { dataArray[47] = a }
    def getARRAmount() { return toNum(dataArray[48]) }
    def setARRAmount(a) { dataArray[48] = a }
    def getNetAmount() { return toNum(dataArray[49]) }
    def setNetAmount(a) { dataArray[49] = a }
    def getHeroOpportunityFlag() { return dataArray[50] }
    def setHeroOpportunityFlag(f) { dataArray[50] = f }
    def getPresalesConcern() { return dataArray[51] }
    def setPresalesConcern(c) { dataArray[51] = c }
    def getPresalesStage() { return dataArray[52] }
    def setPresalesStage(s) { dataArray[52] = s }
    def getTechnicalDifferentiation() { return dataArray[53] }
    def setTechnicalDifferentiation(d) { dataArray[53] = d }
    def getNewAndUpsellAmount() { return toNum(dataArray[54]) }
    def setNewAndUpsellAmount(a) { dataArray[54] = a }
    def getBaseRenewalAmount() { return toNum(dataArray[55]) }
    def setBaseRenewalAmount(a) { dataArray[55] = a }
    def getExpansionACV() { return toNum(dataArray[56]) }
    def setExpansionACV(a) { dataArray[56] = a }
    def getRenewalMY() { return toNum(dataArray[57]) }
    def setRenewalMY(y) { dataArray[57] = y }
    def getRenewedSubscriptionAmount() { return toNum(dataArray[58]) }
    def setRenewedSubscriptionAmount(a) { dataArray[58] = a }
    def getTotalPartnerUpliftAmount() { return toNum(dataArray[59]) }
    def setTotalPartnerUpliftAmount(a) { dataArray[59] = a }
    def getRMForecast() { return dataArray[60] }
    def setRMForecast(f) { dataArray[60] = f }
    def getPOAmount01() { return dataArray[61] }
    def setPOAmount01(a) { dataArray[61] = a }
    def getPOAmount02() { return dataArray[62] }
    def setPOAmount02(a) { dataArray[62] = a }
    def getPOAmount03() { return dataArray[63] }
    def setPOAmount03(a) { dataArray[63] = a }
    def getPOAmount04() { return dataArray[64] }
    def setPOAmount04(a) { dataArray[64] = a }
    def getPOAmount05() { return dataArray[65] }
    def setPOAmount05(a) { dataArray[65] = a }
    def getPOAmountTotal() { return toNum(dataArray[66]) }
    def setPOAmountTotal(t) { dataArray[66] = t }
    def getMyGeolocationLatitude() { return dataArray[67] }
    def setMyGeolocationLatitude(l) { dataArray[67] = l}
    def getMyGeolocationLongitude() { return dataArray[68] }
    def setMyGeolocationLongitude(l) { dataArray[68] = l }
    def getSAManagerQualification() { return dataArray[69] }
    def setSAManagerQualification(q) { dataArray[69] = q}
    def getSAManagerComments() { return dataArray[70] }
    def setSAManagerComments(c) { dataArray[70] = c }

    def getOpportunityAccountHash() { return (opportunityName + account).md5() }
}