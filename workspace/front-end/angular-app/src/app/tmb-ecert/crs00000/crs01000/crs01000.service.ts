import { Injectable } from '@angular/core';
import { AjaxService, DropdownService } from 'app/baiwa/common/services';
import { NgForm, FormGroup, FormControl, Validators } from "@angular/forms";
import { Observable } from "rxjs";
import { dateLocale } from "helpers/";
import { HttpClient } from '@angular/common/http';



@Injectable()
export class Crs01000Service {

  dropdownObj: any;

  constructor(
    private ajax: AjaxService,
    private dropdown: DropdownService) {
  }

  getActionDropdown(){
    return this.dropdown.getaction();
  }

  getReqDate(): string {
    let date = new Date();
    return dateLocale(date);
  }

}

