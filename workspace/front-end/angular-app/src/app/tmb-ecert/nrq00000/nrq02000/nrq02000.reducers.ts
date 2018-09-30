import { Certificate } from "tmb-ecert/models";
import * as Nrq02000Actions from './nrq02000.actions';

export function certificateReducer(state: Certificate[] = [], action: Nrq02000Actions.CertificateActions): Certificate[] {
    switch(action.type) {
        case Nrq02000Actions.CER_CRE:
            state = [...action.payload];
            return state;
        case Nrq02000Actions.CER_UDT:
            state = [...action.payload];
            return state;
        case Nrq02000Actions.CER_RST:
            state = [];
            return state;
        default:
            return state;
    }
}

/**
 * Initial Mock Data
 */
const reqTypeSelectMock: Certificate[] = [
    { code: "10001", typeCode: "50001", typeDesc: "ห้างหุ้นส่วนจำกัด บริษัทจำกัดและบริษัทมหาชนจำกัด", certificate: "หนังสือรับรองนิติบุคคล", feeDbd: "ฉบับละ 200 บาท", feeTmb: "ฉบับละ 150 บาท" },
    { code: "10002", typeCode: "50001", typeDesc: "ห้างหุ้นส่วนจำกัด บริษัทจำกัดและบริษัทมหาชนจำกัด", certificate: "รับรองสำเนาเอกสารทะเบียน", feeDbd: "ฉบับละ 50 บาท", feeTmb: "1-5 หน้าแรก 100 บาทหน้าถัดไปหน้าละ 20 บาท" },
    { code: "10003", typeCode: "50001", typeDesc: "ห้างหุ้นส่วนจำกัด บริษัทจำกัดและบริษัทมหาชนจำกัด", certificate: "รับรองสำเนางบการเงิน", feeDbd: "ฉบับละ 50 บาท", feeTmb: "1-5 หน้าแรก 100 บาทหน้าถัดไปหน้าละ 20 บาท" },
    { code: "10004", typeCode: "50001", typeDesc: "ห้างหุ้นส่วนจำกัด บริษัทจำกัดและบริษัทมหาชนจำกัด", certificate: "รับรองสำเนาบัญชีรายชื่อผู้ถือหุ้น", feeDbd: "ฉบับละ 50 บาท", feeTmb: "1-5 หน้าแรก 100 บาทหน้าถัดไปหน้าละ 20 บาท" },
    { code: "20001", typeCode: "50002", typeDesc: "การประกอบธุรกิจของคนต่างด้าว", certificate: "หนังสือรับรองข้อความที่กรมทะเบียนเก็บรักษาไว้ของการประกอบธุรกิจของคนต่างด้าว", feeDbd: "ฉบับละ 300-500 บาท", feeTmb: "ฉบับละ 150 บาท" },
    { code: "20002", typeCode: "50002", typeDesc: "การประกอบธุรกิจของคนต่างด้าว", certificate: "รับรองสำเนาเอกสารทะเบียน", feeDbd: "หน้าละ 100 บาท", feeTmb: "1-5 หน้าแรก 100 บาทหน้าถัดไปหน้าละ 20 บาท" },
    { code: "20003", typeCode: "50002", typeDesc: "การประกอบธุรกิจของคนต่างด้าว", certificate: "รับรองสำเนางบการเงิน", feeDbd: "หน้าละ 50 บาท", feeTmb: "1-5 หน้าแรก 100 บาทหน้าถัดไปหน้าละ 20 บาท" },
    { code: "30001", typeCode: "50003", typeDesc: "สมาคมและหอการค้า", certificate: "รับรองสำเนาเอกสารทะเบียน", feeDbd: "เรื่องละ 20 บาท", feeTmb: "1-5 หน้าแรก 100 บาทหน้าถัดไปหน้าละ 20 บาท" },
    { code: "30002", typeCode: "50003", typeDesc: "สมาคมและหอการค้า", certificate: "รับรองสำเนางบการเงิน", feeDbd: "เรื่องละ 20 บาท", feeTmb: "1-5 หน้าแรก 100 บาทหน้าถัดไปหน้าละ 20 บาท" }
];