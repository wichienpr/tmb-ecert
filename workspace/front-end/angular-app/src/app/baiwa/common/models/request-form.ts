export interface RequestForm {
    reqFormId: Number
    requestDate: Date
    tmbRequestNo: String
    cerTypeCode: String
    organizeId: String
    customerName: String
    branch: String
    custsegmentCode: String
    caNumber: String
    department: String
    paidTypeCode: String
    debitAccountType: String
    tranCode: String
    glType: String
    accountType: String
    accountNo: String
    accountName: String
    customerNameReceipt: String
    telephone: String
    requestFormFile: String
    idCardFile: String
    changeNameFile: String
    certificateFile: String
    address: String
    remark: String
    paymentDate: Date
    payLoadTs: Date
    paymentBranchCode: String
    postDate: Date
    ref1: String
    ref2: String
    amount: Number
    amountTmb: Number
    amountDbd: Number
    receiptNo: String
    status: String
    errorDescription: String
    paymentStatus: String
    countPayment: Number
    rejectReasonCode: String
    rejectReasonOther: String
    createdById: String
    createdByName: String
    createdDateTime: Date
    updatedById: String
    updatedByName: String
    updatedDateTime: Date
    makerById: String
    makerByName: String
    checkerById: String
    checkerByName: String
    companyName: String
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
};