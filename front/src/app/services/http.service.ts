import { Injectable } from '@angular/core';
import { Todo } from '../models/todo';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Page } from '../models/page';

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  todoApi= "http://localhost:1200/api/todo/";
  constructor(private httpClient: HttpClient) { }

  createTodo(todo: Todo): Observable<Todo>{
    return this.httpClient.post<Todo>(this.todoApi+"add", todo);
  }
  getTodo(): Observable<Todo[]>{
    return this.httpClient.get<GetTodoResponse>(this.todoApi+"all")
    .pipe(map(
      (data)=>{
        return data.data.todos;
      }
    ));
  }
  getTodoWithPagination(pageNum: number, size: number): Observable<GetTodoResponse>{
    const url= this.todoApi+"get?page="+pageNum+"&size="+size;
    return this.httpClient.get<GetTodoResponse>(url);
  }
  deleteTodo(id: number): Observable<Todo> {
    return this.httpClient.delete<Todo>(this.todoApi+"delete/"+id)
  }
  updateTodo(todo: Todo){
    return this.httpClient.put<Todo>(this.todoApi+"update", todo);
  }
  patchTodoStatus(id: number, completedStatus: boolean): Observable<Todo>{
    return this.httpClient.patch<Todo>(this.todoApi+"patch/"+id, {
      completed: completedStatus
    }); 
  }
}
interface GetTodoResponse{
  data: {
    todos: Todo[],
    page: Page
  },
}
