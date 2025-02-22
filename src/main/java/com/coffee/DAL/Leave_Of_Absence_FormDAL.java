package com.coffee.DAL;

import com.coffee.DTO.Leave_Of_Absence_Form;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Leave_Of_Absence_FormDAL extends Manager {
    public Leave_Of_Absence_FormDAL() {
        super("leave_of_absence_form",
                List.of("id",
                        "staff_id",
                        "date",
                        "date_off",
                        "shifts",
                        "reason",
                        "status"
                ));
    }

    public List<Leave_Of_Absence_Form> convertToLeave_Of_Absence_Forms(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Leave_Of_Absence_Form(
                        Integer.parseInt(row.get(0)), // id
                        Integer.parseInt(row.get(1)), // staff_id
                        Date.valueOf(row.get(2)), // date
                        Date.valueOf(row.get(3)), // date_off
                        row.get(4), // end_date
                        row.get(5), // reason
                        Integer.parseInt(row.get(6)) // status
                );
            } catch (Exception e) {
                System.out.println("Error occurred in Leave_Of_Absence_FormDAL.convertToLeave_Of_Absence_Forms(): " + e.getMessage());
            }
            return new Leave_Of_Absence_Form();
        });
    }

    public int addLeave_Of_Absence_Form(Leave_Of_Absence_Form leave_Of_Absence_Form) {
        try {
            return create(leave_Of_Absence_Form.getId(),
                    leave_Of_Absence_Form.getStaff_id(),
                    leave_Of_Absence_Form.getDate(),
                    leave_Of_Absence_Form.getDate_off(),
                    leave_Of_Absence_Form.getShifts(),
                    leave_Of_Absence_Form.getReason(),
                    0
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Leave_Of_Absence_FormDAL.addLeave_Of_Absence_Form(): " + e.getMessage());
        }
        return 0;
    }

    public int updateLeave_Of_Absence_Form(Leave_Of_Absence_Form leave_Of_Absence_Form) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(leave_Of_Absence_Form.getId());
            updateValues.add(leave_Of_Absence_Form.getStaff_id());
            updateValues.add(leave_Of_Absence_Form.getDate());
            updateValues.add(leave_Of_Absence_Form.getDate_off());
            updateValues.add(leave_Of_Absence_Form.getShifts());
            updateValues.add(leave_Of_Absence_Form.getReason());
            updateValues.add(leave_Of_Absence_Form.getStatus());
            return update(updateValues, "id = " + leave_Of_Absence_Form.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Leave_Of_Absence_FormDAL.updateLeave_Of_Absence_Form(): " + e.getMessage());
        }
        return 0;
    }

    public List<Leave_Of_Absence_Form> searchLeave_Of_Absence_Forms(String... conditions) {
        try {
            return convertToLeave_Of_Absence_Forms(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Leave_Of_Absence_FormDAL.searchLeave_Of_Absence_Forms(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
