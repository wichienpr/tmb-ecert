import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

declare var $: any;

@Component({
  selector: 'app-nrq000200',
  templateUrl: './nrq000200.component.html',
  styleUrls: ['./nrq000200.component.css']
})
export class Nrq000200Component implements OnInit {

  constructor() { }

  ngOnInit() {
    $('#modal-confirm').modal({centered: false}).modal('show');
  }
  modalConfirm(){
    $('#modal-confirm').modal('hide');
    // setTimeout(() => {
    //   $('#modal-request').modal({centered: false}).modal('show');
    // }, 300);
    
  }
  modalCancel() {
    $('#modal-confirm').modal('hide');
  }


}
