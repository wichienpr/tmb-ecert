import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";

declare var $: any;

@Injectable()
export class DialogService {
  confirm(message?: string): Observable<boolean> {
    const confirmation = window.confirm(message || 'คุณแน่ใจหรือไม่?');
    return of(confirmation);
  };
} 