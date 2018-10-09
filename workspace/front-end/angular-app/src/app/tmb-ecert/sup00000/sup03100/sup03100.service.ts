import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AjaxService } from 'app/baiwa/common/services';
import { Sup03100 } from 'app/tmb-ecert/sup00000/sup03100/sup03100.model';

const URL = {
   SUP03100_SEARCH: "/api/setup/sup03000/getEmailDetail",
   SUP03000_SAVE: "/api/setup/sup03000/saveEmailDetail"
}

@Injectable({
   providedIn: 'root'
})
export class Sup03100Service {

   constructor(private ajax: AjaxService, private httpClient: HttpClient, ) {

   }

   callSearchEmailDetailAPI(id) {
      return this.httpClient.post(AjaxService.CONTEXT_PATH + URL.SUP03100_SEARCH, {
         emailConfig_id: id,
         name: "",
         status: ""
      });
   }

   callSaveEmailDetailAPI(sup03100:Sup03100) {
      return this.httpClient.post(AjaxService.CONTEXT_PATH + URL.SUP03000_SAVE, sup03100);

   }

}
