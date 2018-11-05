import { Injectable } from "@angular/core";
import { Lov } from "models/";
import { Observable } from "rxjs";
import { AjaxService } from "./ajax.service";

const URL = {
    LOV_BY_TYPE: "/api/lov/type"
}

@Injectable()
export class DropdownService { // TABLE => ECERT_LISTOFVALUE
    private reqType: Lov[]; // ประเภทคำขอ
    private customSeg: Lov[]; // Customer Segment
    private payMethod: Lov[]; // วิธีชำระ
    private subAccMethod: Lov[]; // วีธีหักจากธนาคาร
    private action: Lov[]; // Action
    private operationTypeLOV: Lov[]; // สถานะการทำงาน
    private batchJobSchLOV: Lov[]; // BatchJobSchedu
    private jobMonitoringStus: Lov[];  //Monitoring Status
    private rejectReason: Lov[];  //RejectReason

    constructor(private ajax: AjaxService) { }

    getReqType(): Observable<Lov[]> { // ประเภทคำขอ
        return new Observable(obs => {
            this.ajax.post(URL.LOV_BY_TYPE, { type: 5 }, result => {
                let data = [];
                if (result) {
                    data = result.json();
                }
                if (data && data.length > 0) {
                    this.reqType = data;
                }
                obs.next([...this.reqType]);
                obs.complete();
            });
        });
    }

    getCustomSeg(): Observable<Lov[]> { // Customer Segment
        return new Observable(obs => {
            this.ajax.post(URL.LOV_BY_TYPE, { type: 2 }, result => {
                let data = [];
                if (result) {
                    data = result.json();
                }
                if (data && data.length > 0) {
                    this.customSeg = data;
                }
                obs.next([...this.customSeg]);
                obs.complete();
            });
        });
    }

    getpayMethod(): Observable<Lov[]> { // วิธีชำระ
        return new Observable(obs => {
            this.ajax.post(URL.LOV_BY_TYPE, { type: 3 }, result => {
                let data = [];
                if (result) {
                    data = result.json();
                }
                if (data && data.length > 0) {
                    this.payMethod = data;
                }
                obs.next([...this.payMethod]);
                obs.complete();
            });
        });
    }

    getsubAccMethod(): Observable<Lov[]> { // วีธีหักจากธนาคาร
        return new Observable(obs => {
            this.ajax.post(URL.LOV_BY_TYPE, { type: 4 }, result => {
                let data = [];
                if (result) {
                    data = result.json();
                }
                if (data && data.length > 0) {
                    this.subAccMethod = data;
                }
                obs.next([...this.subAccMethod]);
                obs.complete();
            });
        });
    }

    getaction(): Observable<Lov[]> { // Action
        return new Observable(obs => {
            this.ajax.post(URL.LOV_BY_TYPE, { type: 7 }, result => {
                let data = [];
                if (result) {
                    data = result.json();
                }
                if (data && data.length > 0) {
                    this.action = data;
                }
                obs.next([...this.action]);
                obs.complete();
            });
        });
    }

    getStatusType(): Observable<Lov[]> { // สถานะการทำงาน
        return new Observable(obs => {
            this.ajax.post(URL.LOV_BY_TYPE, { type: 9 }, result => {
                let data = [];
                if (result) {
                    data = result.json();
                }
                if (data && data.length > 0) {
                    this.operationTypeLOV = data;
                }
                obs.next([...this.operationTypeLOV]);
                obs.complete();
            });
        });
    }

    getBatchJobSchedu(): Observable<Lov[]> { // Batch Job Scheduler
        return new Observable(obs => {
            this.ajax.post(URL.LOV_BY_TYPE, { type: 6 }, result => {
                let data = [];
                if (result) {
                    data = result.json();
                }
                if (data && data.length > 0) {
                    this.batchJobSchLOV = data;
                }
                obs.next([...this.batchJobSchLOV]);
                obs.complete();
            });
        });
    }

    getMonitoringStatus(): Observable<Lov[]> { // Monitoring Status
        return new Observable(obs => {
            this.ajax.post(URL.LOV_BY_TYPE, { type: 8 }, result => {
                let data = [];
                if (result) {
                    data = result.json();
                }
                if (data && data.length > 0) {
                    this.jobMonitoringStus = data;
                }
                obs.next([...this.jobMonitoringStus]);
                obs.complete();
            });
        });
    }

    getRejectReason(): Observable<Lov[]> {
        return new Observable(obs => {
            this.ajax.post(URL.LOV_BY_TYPE, { type: 11 }, result => {
                let data = [];
                if (result) {
                    data = result.json();
                }
                if (data && data.length > 0) {
                    this.rejectReason = data;
                }
                obs.next([...this.rejectReason]);
                obs.complete();
            });
        });
    }

}