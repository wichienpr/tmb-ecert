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
	statementYear?: number
	acceptedDate?: Date
	other?: string
}