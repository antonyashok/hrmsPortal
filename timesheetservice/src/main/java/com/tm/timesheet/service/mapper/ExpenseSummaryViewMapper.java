package com.tm.timesheet.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.timesheet.domain.ExpenseDetailedView;
import com.tm.timesheet.domain.ExpenseSummaryView;
import com.tm.timesheet.service.dto.ExpenseDetailedViewDTO;
import com.tm.timesheet.service.dto.ExpenseSummaryViewDTO;


@Mapper
public interface ExpenseSummaryViewMapper {

	ExpenseSummaryViewMapper INSTANCE = Mappers.getMapper(ExpenseSummaryViewMapper.class);

	ExpenseSummaryViewDTO expenseSummaryViewToExpenseSummaryViewDTO(ExpenseSummaryView expenseSummaryView);

	ExpenseDetailedViewDTO expenseDetiledViewToExpenseDetiledDTO(ExpenseDetailedView expensesDetailedView);
}
