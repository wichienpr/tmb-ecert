import { Injectable } from '@angular/core';
import { AjaxService } from 'services/';

const URL = {
  TEST: "/api/cer/typeCode"
};

@Injectable({
  providedIn: 'root'
})
export class Ex4Service {

  constructor(private ajax: AjaxService) {
    
  }

  findByCode(typeCode: string) {
    this.ajax.post(URL.TEST, { typeCode: typeCode }, response => {
      console.log(response);
    })
  }

}
