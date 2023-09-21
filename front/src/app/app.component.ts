import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from '@angular/material/snack-bar';
import { Todo } from './models/todo';
import { HttpService } from './services/http.service';
import { MESSAGE } from './client.data';
import { GeneratePage } from './models/page';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
  readonly MESSAGE= MESSAGE;

  horizentalPosition: MatSnackBarHorizontalPosition= 'end';
  verticalPosition: MatSnackBarVerticalPosition= 'top';

  todoForm!: FormGroup;
  todos: Todo[]= [];
  isEditMode: boolean= false;

  pageNum= 0;
  size= 5;
  generatedPages: GeneratePage[]= [];

  constructor(private snackBar: MatSnackBar, 
              private formBuilder: FormBuilder,
              private httpService: HttpService){

  }
  ngOnInit(): void {
    this.todoForm= this.formBuilder.group({
      id: [''],
      name: ['', Validators.required],
      description: ['', Validators.required],
      completed: [''],
    })
    this.getTodoWithPagination();
  }

  getTodo(){
    this.httpService.getTodo().subscribe(
      (data)=>{
        this.todos= data;
      }
    )
  }
  getTodoWithPagination(page= 1){
    this.httpService.getTodoWithPagination(page, this.size).subscribe(
      (data)=> {
        if(data){
          this.todos= data.data.todos;
          this.generateAllPages(data.data.page.totalPages);
          this.pageNum= data.data.page.number;
        }
      }
    )
  }
  generateAllPages(totalPages: number){
    this.generatedPages= [];
    for(let i= 0; i< totalPages; i++){
      this.generatedPages.push({
        displayValue: i+1,
        value: i
      })
    }
  }
  deleteTodo(id: number){
    if(confirm("Are you sure to delete this todo!?")){
      this.httpService.deleteTodo(id).subscribe(
        (datea)=>{
          this.getTodoWithPagination(this.pageNum);
          this.openSnackBar(MESSAGE.DELETED)
        }
      );
    }
  }

  updateTodo(todo: Todo){
    this.httpService.updateTodo(todo).subscribe(
      (data)=> {
        this.getTodoWithPagination(this.pageNum);
        this.todoForm.reset();

        this.openSnackBar(MESSAGE.UPDATED)
      }
    )
  }

  patchTodoStatus(id: number, completedStatus: boolean){
    this.httpService.patchTodoStatus(id, completedStatus).subscribe(
      (data)=> {
        this.getTodoWithPagination(this.pageNum);
        this.openSnackBar(MESSAGE.PATCHED)
      }
    )
  }

  handleEdit(todo: Todo){
     this.isEditMode= true;
     delete todo.dateCreated;
     delete todo.lastUpdated;
     this.todoForm.setValue(todo);
  }
   openSnackBar(msg: string){
    this.snackBar.open((msg), 'X', {
      horizontalPosition: this.horizentalPosition,
      verticalPosition: this.verticalPosition,
      duration: 2000
    });
  }
  
  onSubmit(){
    if(this.todoForm.invalid){
      return;
    }
    const formValue: Todo= this.todoForm.value;
    if(this.isEditMode){
      this.updateTodo(formValue);
      this.isEditMode= false;
    } else {
      const todoRequest: Todo= {
        name: formValue.name,
        description: formValue.description,
        completed: false
      }
      this.httpService.createTodo(todoRequest).subscribe(
        (date)=> {
          this.getTodoWithPagination(this.pageNum)
          this.openSnackBar(MESSAGE.CREATED)
        }
      )
    }
  }

}
