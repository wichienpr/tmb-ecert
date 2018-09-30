import { Component, OnInit } from '@angular/core';
import { Crs01000Service } from 'app/tmb-ecert/crs00000/crs01000/crs01000.service';



declare var $: any;
@Component({
  selector: 'app-crs01000',
  templateUrl: './crs01000.component.html',
  styleUrls: ['./crs01000.component.css'],
 
})
export class Crs01000Component implements OnInit {
  
  reqDate:string='';
  toDate:string='';
  companyName: string='';
  organizeId:string='';
  tmbReqNo:string='';
  status: string ='';

  requestForm



  constructor(private crs01000Service : Crs01000Service) { 
    
  }

  ngOnInit() {
    this.crs01000Service.findAll();
  }
 
  ngAfterViewInit() {
    $('.ui.sidebar')
      .sidebar({
        context: '.ui.grid.pushable'
      })
      .sidebar('setting', 'transition', 'push')
      .sidebar('toggle');
  }

  onToggle() {
    $('.ui.sidebar')
      .sidebar({
        context: '.ui.grid.pushable'
      })
      .sidebar('setting', 'transition', 'push')
      .sidebar('toggle');
  }

}

class RequestForm {
  
}