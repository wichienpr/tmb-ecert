import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

declare var $: any;

@Component({
  selector: 'app-nrq02000',
  templateUrl: './nrq02000.component.html',
  styleUrls: ['./nrq02000.component.css']
})
export class Nrq02000Component implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() { }

  send() {
    $('#send-req').modal('show');
  }

  redirect() {
    $('#send-req').modal('hide');
    this.router.navigate(['performa']);
  }

}
