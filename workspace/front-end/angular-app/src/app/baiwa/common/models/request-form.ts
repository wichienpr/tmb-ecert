export interface RequestForm {
    [x: string]: any
    reqFormId: number
    requestDate: Date
    tmbRequestNo: string
    cerTypeCode: string
    organizeId: string
    customerName: string
    branch: string
    custsegmentCode: string
    caNumber: string
    department: string
    paidTypeCode: string
    debitAccountType: string
    tranCode: string
    glType: string
    accountType: string
    accountNo: string
    accountName: string
    customerNameReceipt: string
    telephone: string
    requestFormFile: string
    idCardFile: string
    changeNameFile: string
    certificateFile: string
    address: string
    remark: string
    paymentDate: Date
    payLoadTs: Date
    paymentBranchCode: string
    postDate: Date
    ref1: string
    ref2: string
    amount: number
    amountTmb: number
    amountDbd: number
    receiptFile: string
    receiptNo: string
    status: string
    errorDescription: string
    paymentStatus: string
    countPayment: number
    rejectReasonCode: string
    rejectReasonOther: string
    createdById: string
    createdByName: string
    createdDateTime: Date
    updatedById: string
    updatedByName: string
    updatedDateTime: Date
    makerById: string
    makerByName: string
    checkerById: string
    checkerByName: string
    companyName: string
    lockFlag: number
}

export const initRequestForm: RequestForm = {
    reqFormId: 0,
    requestDate: new Date(),
    tmbRequestNo: "",
    cerTypeCode: "",
    organizeId: "",
    customerName: "",
    branch: "",
    custsegmentCode: "",
    caNumber: "",
    department: "",
    paidTypeCode: "",
    debitAccountType: "",
    tranCode: "",
    glType: "",
    accountType: "",
    accountNo: "",
    accountName: "",
    customerNameReceipt: "",
    telephone: "",
    requestFormFile: "",
    idCardFile: "",
    changeNameFile: "",
    certificateFile: "",
    address: "",
    remark: "",
    paymentDate: new Date(),
    payLoadTs: new Date(),
    paymentBranchCode: "",
    postDate: new Date(),
    ref1: "",
    ref2: "",
    amount: 0,
    amountTmb: 0,
    amountDbd: 0,
    receiptFile: "",
    receiptNo: "",
    status: "",
    errorDescription: "",
    paymentStatus: "",
    countPayment: 0,
    rejectReasonCode: "",
    rejectReasonOther: "",
    createdById: "",
    createdByName: "",
    createdDateTime: new Date(),
    updatedById: "",
    updatedByName: "",
    updatedDateTime: new Date(),
    makerById: "",
    makerByName: "",
    checkerById: "",
    checkerByName: "",
    companyName: "",
    lockFlag: 0,
};