export interface RequestCertificate {
    reqCertificateId: number
	reqFormId: number
	certificateCode: string
	totalNumber: number
	createdById: string
	createdByName: string
	createdDateTime: Date
	updateById: string
	updateByName: string
	updateDateTime: Date
	registeredDate?: Date
	statementYear?: string
	acceptedDate?: Date
	other?: string
}