package com.example.model;

public enum UserRole {
    ADMIN,
    USER,
    GUEST,
    MANAGER,
    SALESMAN,
    PURCHASE_OFFICER,
    ACCOUNTANT,
    CUSTOMER
    }

/*
*
*
ADMIN, add/delete/modify items, customers, merchants, stores, branches, users with roles.
needed features, stores, branches management, users and roles

BRANCH-MANAGER, approve his customers, show request, branch only details.

SALESMAN, make a SO to his are, show SOs in his area only. -> should be integrated with POS machines, or it should be handled by the branch manager himself,
PURCHASE_OFFICER, make a PO show POs,
ACCOUNTANT, check po/sos totals and remain, salaries payroll, quarter profit,
HR, add/modify, salaries, vacations,
CUSTOMER, only sees his own data,

*
*
* */
