import { Injectable } from '@angular/core';
import { NgForm, FormGroup, FormControl, Validators } from "@angular/forms";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class Srn01000Service {

  form: FormGroup = new FormGroup({
    tmbReqNo: new FormControl('', Validators.required),
    status: new FormControl('',)
  });


  constructor() { }

      /**
     * Initial Data
     */
    getForm(): Observable<FormGroup> {
      return new Observable<FormGroup>(obs => {
          obs.next(this.form);
      });
  }


}
