package barretta.elastic.vivun.objects

class Opportunity extends VivunCsvObject{
    def Opportunity(String[] data) {
        super.dataArray = data
    }

    def getOpportunityName() { return dataArray[0] }
    def getAccount() { return dataArray[1] }
    def GetOwnerID() { return dataArray[2] }
    def getCloseDate() { return dataArray[3] }
    def getStage() { return dataArray[4] }
    def getForecastCategory() { return dataArray[5] }
    def getHeroScore() { return dataArray[6] }
    def getOpportunityId() { return dataArray[7] }
    def getDeleted() { return dataArray[8] }
    def getRecordType() { return dataArray[9] }
    def getPrivate() { return dataArray[10] }
    def getDescription() { return dataArray[11] }
    def getAmount() { return toNum(dataArray[12])}
    def getProbability() { return dataArray[13] }
    def getExpectedAmount() { return dataArray[14] }
    def getOpportunityType() { return dataArray[15] }
    def getNextStep() { return dataArray[16] }
    def getLeadSource() { return dataArray[17] }
    def getClosed() { return dataArray[18] }
    def getWon() { return dataArray[19] }
    def getOpportunityCurrency() { return dataArray[20] }
    def getCampaign() { return dataArray[21] }
    def getIsSplit() { return dataArray[22] }
    def getPriceBook() { return dataArray[23] }
    def getTerritory() { return dataArray[24] }
    def getExcludeFromTheTerritoryAssignmentFilterLogic() { return dataArray[25] }
    def getCreatedDate() { return dataArray[26] }
    def getCreatedBy() { return dataArray[27] }
    def getLastModifiedDate() { return dataArray[28] }
    def getLastModifiedBy() { return dataArray[29] }
    def getSystemModstamp() { return dataArray[30] }
    def getLastActivity() { return dataArray[31] }
    def getFiscalQuarter() { return dataArray[32] }
    def getFiscalYear() { return dataArray[33] }
    def getFiscalPeriod() { return dataArray[34] }
    def getLastViewedDate() { return dataArray[35] }
    def getLastReferencedDate() { return dataArray[36] }
    def getHasOpenActivity() { return dataArray[37] }
    def getHasOverdueTask() { return dataArray[38] }
    def getBalance() { return dataArray[39] }
    def getContractAmount() { return dataArray[40] }
    def getCanRevive() { return dataArray[41] }
    def getDateRevived() { return dataArray[42] }
    def getACVUpsellAttrition() { return dataArray[43] }
    def getTCVAmount() { return dataArray[44] }
    def getGreatestOppLineAmount() { return dataArray[45] }
    def getARRAmount() { return dataArray[46] }
    def getNetAmount() { return dataArray[47] }
    def getHeroOpportunityFlag() { return dataArray[48] }
    def getPresalesConcern() { return dataArray[49] }
    def getPresalesStage() { return dataArray[50] }
    def getTechnicalDifferentiation() { return dataArray[51] }
    def getNewAndUpsellAmount() { return toNum(dataArray[52]) }
    def getBaseRenewalAmount() { return toNum(dataArray[53]) }
    def getExpansionACV() { return toNum(dataArray[54]) }
    def getRenewalMY() { return toNum(dataArray[55]) }
    def getRenewedSubscriptionAmount() { return toNum(dataArray[56]) }
    def getTotalPartnerUpliftAmount() { return toNum(dataArray[57]) }
    def getRMForecast() { return dataArray[58] }
    def getPOAmount01() { return dataArray[59] }
    def getPOAmount02() { return dataArray[60] }
    def getPOAmount03() { return dataArray[61] }
    def getPOAmount04() { return dataArray[62] }
    def getPOAmount05() { return dataArray[63] }
    def getPOAmountTotal() { return toNum([64]) }
    def getMyGeolocationLatitude() { return dataArray[65] }
    def getMyGeolocationLongitude() { return dataArray[66] }
    def getSAManagerQualification() { return dataArray[67] }
    def getSAManagerComments() { return dataArray[68] }
}