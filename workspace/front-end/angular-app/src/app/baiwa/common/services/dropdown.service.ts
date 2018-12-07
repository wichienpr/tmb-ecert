import { Injectable } from "@angular/core";
import { Lov } from "models/";
import { Observable, throwError } from "rxjs";
import { AjaxService } from "./ajax.service";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { retry, catchError } from "rxjs/operators";

const URL = {
    LOV_BY_TYPE: AjaxService.CONTEXT_PATH + "/api/lov/type"
}

@Injectable()
export class DropdownService { // TABLE => ECERT_LISTOFVALUE

    constructor(private http: HttpClient) { }

    getReqType(): Observable<Lov[]> { // ประเภทคำขอ
        return this.http.post<Lov[]>(URL.LOV_BY_TYPE, { type: 5 })
            .pipe(
                retry(3), catchError(this.handleError)
            );
    }

    getCustomSeg(): Observable<Lov[]> { // Customer Segment
        return this.http.post<Lov[]>(URL.LOV_BY_TYPE, { type: 2 })
            .pipe(
                retry(3), catchError(this.handleError)
            );
    }

    getpayMethod(): Observable<Lov[]> { // วิธีชำระ
        return this.http.post<Lov[]>(URL.LOV_BY_TYPE, { type: 3 })
            .pipe(
                retry(3), catchError(this.handleError)
            );
    }

    getsubAccMethod(): Observable<Lov[]> { // วีธีหักจากธนาคาร
        return this.http.post<Lov[]>(URL.LOV_BY_TYPE, { type: 4 })
            .pipe(
                retry(3), catchError(this.handleError)
            );
    }

    getaction(): Observable<Lov[]> { // Action
        return this.http.post<Lov[]>(URL.LOV_BY_TYPE, { type: 7 })
            .pipe(
                retry(3), catchError(this.handleError)
            );
    }

    getStatusType(): Observable<Lov[]> { // สถานะการทำงาน
        return this.http.post<Lov[]>(URL.LOV_BY_TYPE, { type: 9 })
            .pipe(
                retry(3), catchError(this.handleError)
            );
    }

    getBatchJobSchedu(): Observable<Lov[]> { // Batch Job Scheduler
        return this.http.post<Lov[]>(URL.LOV_BY_TYPE, { type: 6 })
            .pipe(
                retry(3), catchError(this.handleError)
            );
    }

    getMonitoringStatus(): Observable<Lov[]> { // Monitoring Status
        return this.http.post<Lov[]>(URL.LOV_BY_TYPE, { type: 8 })
            .pipe(
                retry(3), catchError(this.handleError)
            );
    }

    getRejectReason(): Observable<Lov[]> {
        return this.http.post<Lov[]>(URL.LOV_BY_TYPE, { type: 11 })
            .pipe(
                retry(3), catchError(this.handleError)
            );
    }

    getParameterGroup(): Observable<Lov[]> {
        return this.http.post<Lov[]>(URL.LOV_BY_TYPE, { type: 12 })
            .pipe(
                retry(3), catchError(this.handleError)
            );
    }

    private handleError(error: HttpErrorResponse) {
        if (error.error instanceof ErrorEvent) {
            // A client-side or network error occurred. Handle it accordingly.
            console.error('An error occurred:', error.error.message);
        } else {
            // The backend returned an unsuccessful response code.
            // The response body may contain clues as to what went wrong,
            console.error(
                `Backend returned code ${error.status}, ` +
                `body was: ${error.error}`);
        }
        // return an observable with a user-facing error message
        return throwError(
            'มีบางอย่างผิดพลาด; กรุณาลองอีกครั้ง.');
    };

}