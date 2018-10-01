import { Injectable } from '@angular/core';
import { AjaxService } from 'app/baiwa/common/services';

const URL={
  testList:"crs/crs01000/list",
  findAll:"crs/crs01000/findAll"
};


@Injectable({
  providedIn: 'root'
})


export class Crs01000Service {

  constructor(
    private ajax: AjaxService) {

    
   }


   testList() {
    this.ajax.post(URL.testList,{},res =>{
      console.log(res.json());
    });
   }

   findAll() {
    this.ajax.post(URL.findAll,{},res =>{
      console.log(res.json());
    });
   }

}
