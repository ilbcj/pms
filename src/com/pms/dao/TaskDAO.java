package com.pms.dao;

import java.util.List;

import com.pms.model.Task;

public interface TaskDAO {
	public void TaskAdd(Task task) throws Exception;
	public void TaskMod(Task task) throws Exception;
	//public void TaskDel(Task task) throws Exception;
	public Task GetTaskById(int id) throws Exception;
	public List<Task> GetTasks(Task criteria, int start, int length) throws Exception;
}
