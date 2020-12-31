package com.example.demo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OperateLogExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OperateLogExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("T_OPERATE_LOG.ID is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("T_OPERATE_LOG.ID is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.ID =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.ID <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("T_OPERATE_LOG.ID >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.ID >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("T_OPERATE_LOG.ID <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.ID <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("T_OPERATE_LOG.ID like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("T_OPERATE_LOG.ID not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("T_OPERATE_LOG.ID in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("T_OPERATE_LOG.ID not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("T_OPERATE_LOG.ID between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("T_OPERATE_LOG.ID not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andOperateTableIsNull() {
            addCriterion("T_OPERATE_LOG.OPERATE_TABLE is null");
            return (Criteria) this;
        }

        public Criteria andOperateTableIsNotNull() {
            addCriterion("T_OPERATE_LOG.OPERATE_TABLE is not null");
            return (Criteria) this;
        }

        public Criteria andOperateTableEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_TABLE =", value, "operateTable");
            return (Criteria) this;
        }

        public Criteria andOperateTableNotEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_TABLE <>", value, "operateTable");
            return (Criteria) this;
        }

        public Criteria andOperateTableGreaterThan(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_TABLE >", value, "operateTable");
            return (Criteria) this;
        }

        public Criteria andOperateTableGreaterThanOrEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_TABLE >=", value, "operateTable");
            return (Criteria) this;
        }

        public Criteria andOperateTableLessThan(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_TABLE <", value, "operateTable");
            return (Criteria) this;
        }

        public Criteria andOperateTableLessThanOrEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_TABLE <=", value, "operateTable");
            return (Criteria) this;
        }

        public Criteria andOperateTableLike(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_TABLE like", value, "operateTable");
            return (Criteria) this;
        }

        public Criteria andOperateTableNotLike(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_TABLE not like", value, "operateTable");
            return (Criteria) this;
        }

        public Criteria andOperateTableIn(List<String> values) {
            addCriterion("T_OPERATE_LOG.OPERATE_TABLE in", values, "operateTable");
            return (Criteria) this;
        }

        public Criteria andOperateTableNotIn(List<String> values) {
            addCriterion("T_OPERATE_LOG.OPERATE_TABLE not in", values, "operateTable");
            return (Criteria) this;
        }

        public Criteria andOperateTableBetween(String value1, String value2) {
            addCriterion("T_OPERATE_LOG.OPERATE_TABLE between", value1, value2, "operateTable");
            return (Criteria) this;
        }

        public Criteria andOperateTableNotBetween(String value1, String value2) {
            addCriterion("T_OPERATE_LOG.OPERATE_TABLE not between", value1, value2, "operateTable");
            return (Criteria) this;
        }

        public Criteria andOperateTypeIsNull() {
            addCriterion("T_OPERATE_LOG.OPERATE_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andOperateTypeIsNotNull() {
            addCriterion("T_OPERATE_LOG.OPERATE_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andOperateTypeEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_TYPE =", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeNotEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_TYPE <>", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeGreaterThan(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_TYPE >", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeGreaterThanOrEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_TYPE >=", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeLessThan(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_TYPE <", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeLessThanOrEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_TYPE <=", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeLike(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_TYPE like", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeNotLike(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_TYPE not like", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeIn(List<String> values) {
            addCriterion("T_OPERATE_LOG.OPERATE_TYPE in", values, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeNotIn(List<String> values) {
            addCriterion("T_OPERATE_LOG.OPERATE_TYPE not in", values, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeBetween(String value1, String value2) {
            addCriterion("T_OPERATE_LOG.OPERATE_TYPE between", value1, value2, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeNotBetween(String value1, String value2) {
            addCriterion("T_OPERATE_LOG.OPERATE_TYPE not between", value1, value2, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateStatusIsNull() {
            addCriterion("T_OPERATE_LOG.OPERATE_STATUS is null");
            return (Criteria) this;
        }

        public Criteria andOperateStatusIsNotNull() {
            addCriterion("T_OPERATE_LOG.OPERATE_STATUS is not null");
            return (Criteria) this;
        }

        public Criteria andOperateStatusEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_STATUS =", value, "operateStatus");
            return (Criteria) this;
        }

        public Criteria andOperateStatusNotEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_STATUS <>", value, "operateStatus");
            return (Criteria) this;
        }

        public Criteria andOperateStatusGreaterThan(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_STATUS >", value, "operateStatus");
            return (Criteria) this;
        }

        public Criteria andOperateStatusGreaterThanOrEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_STATUS >=", value, "operateStatus");
            return (Criteria) this;
        }

        public Criteria andOperateStatusLessThan(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_STATUS <", value, "operateStatus");
            return (Criteria) this;
        }

        public Criteria andOperateStatusLessThanOrEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_STATUS <=", value, "operateStatus");
            return (Criteria) this;
        }

        public Criteria andOperateStatusLike(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_STATUS like", value, "operateStatus");
            return (Criteria) this;
        }

        public Criteria andOperateStatusNotLike(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_STATUS not like", value, "operateStatus");
            return (Criteria) this;
        }

        public Criteria andOperateStatusIn(List<String> values) {
            addCriterion("T_OPERATE_LOG.OPERATE_STATUS in", values, "operateStatus");
            return (Criteria) this;
        }

        public Criteria andOperateStatusNotIn(List<String> values) {
            addCriterion("T_OPERATE_LOG.OPERATE_STATUS not in", values, "operateStatus");
            return (Criteria) this;
        }

        public Criteria andOperateStatusBetween(String value1, String value2) {
            addCriterion("T_OPERATE_LOG.OPERATE_STATUS between", value1, value2, "operateStatus");
            return (Criteria) this;
        }

        public Criteria andOperateStatusNotBetween(String value1, String value2) {
            addCriterion("T_OPERATE_LOG.OPERATE_STATUS not between", value1, value2, "operateStatus");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("T_OPERATE_LOG.CREATE_TIME is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("T_OPERATE_LOG.CREATE_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("T_OPERATE_LOG.CREATE_TIME =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("T_OPERATE_LOG.CREATE_TIME <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("T_OPERATE_LOG.CREATE_TIME >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("T_OPERATE_LOG.CREATE_TIME >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("T_OPERATE_LOG.CREATE_TIME <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("T_OPERATE_LOG.CREATE_TIME <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("T_OPERATE_LOG.CREATE_TIME in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("T_OPERATE_LOG.CREATE_TIME not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("T_OPERATE_LOG.CREATE_TIME between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("T_OPERATE_LOG.CREATE_TIME not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("T_OPERATE_LOG.UPDATE_TIME is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("T_OPERATE_LOG.UPDATE_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("T_OPERATE_LOG.UPDATE_TIME =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("T_OPERATE_LOG.UPDATE_TIME <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("T_OPERATE_LOG.UPDATE_TIME >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("T_OPERATE_LOG.UPDATE_TIME >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("T_OPERATE_LOG.UPDATE_TIME <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("T_OPERATE_LOG.UPDATE_TIME <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("T_OPERATE_LOG.UPDATE_TIME in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("T_OPERATE_LOG.UPDATE_TIME not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("T_OPERATE_LOG.UPDATE_TIME between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("T_OPERATE_LOG.UPDATE_TIME not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andOperateCustomerIsNull() {
            addCriterion("T_OPERATE_LOG.OPERATE_CUSTOMER is null");
            return (Criteria) this;
        }

        public Criteria andOperateCustomerIsNotNull() {
            addCriterion("T_OPERATE_LOG.OPERATE_CUSTOMER is not null");
            return (Criteria) this;
        }

        public Criteria andOperateCustomerEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_CUSTOMER =", value, "operateCustomer");
            return (Criteria) this;
        }

        public Criteria andOperateCustomerNotEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_CUSTOMER <>", value, "operateCustomer");
            return (Criteria) this;
        }

        public Criteria andOperateCustomerGreaterThan(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_CUSTOMER >", value, "operateCustomer");
            return (Criteria) this;
        }

        public Criteria andOperateCustomerGreaterThanOrEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_CUSTOMER >=", value, "operateCustomer");
            return (Criteria) this;
        }

        public Criteria andOperateCustomerLessThan(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_CUSTOMER <", value, "operateCustomer");
            return (Criteria) this;
        }

        public Criteria andOperateCustomerLessThanOrEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_CUSTOMER <=", value, "operateCustomer");
            return (Criteria) this;
        }

        public Criteria andOperateCustomerLike(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_CUSTOMER like", value, "operateCustomer");
            return (Criteria) this;
        }

        public Criteria andOperateCustomerNotLike(String value) {
            addCriterion("T_OPERATE_LOG.OPERATE_CUSTOMER not like", value, "operateCustomer");
            return (Criteria) this;
        }

        public Criteria andOperateCustomerIn(List<String> values) {
            addCriterion("T_OPERATE_LOG.OPERATE_CUSTOMER in", values, "operateCustomer");
            return (Criteria) this;
        }

        public Criteria andOperateCustomerNotIn(List<String> values) {
            addCriterion("T_OPERATE_LOG.OPERATE_CUSTOMER not in", values, "operateCustomer");
            return (Criteria) this;
        }

        public Criteria andOperateCustomerBetween(String value1, String value2) {
            addCriterion("T_OPERATE_LOG.OPERATE_CUSTOMER between", value1, value2, "operateCustomer");
            return (Criteria) this;
        }

        public Criteria andOperateCustomerNotBetween(String value1, String value2) {
            addCriterion("T_OPERATE_LOG.OPERATE_CUSTOMER not between", value1, value2, "operateCustomer");
            return (Criteria) this;
        }

        public Criteria andDataIdIsNull() {
            addCriterion("T_OPERATE_LOG.DATA_ID is null");
            return (Criteria) this;
        }

        public Criteria andDataIdIsNotNull() {
            addCriterion("T_OPERATE_LOG.DATA_ID is not null");
            return (Criteria) this;
        }

        public Criteria andDataIdEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.DATA_ID =", value, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdNotEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.DATA_ID <>", value, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdGreaterThan(String value) {
            addCriterion("T_OPERATE_LOG.DATA_ID >", value, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdGreaterThanOrEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.DATA_ID >=", value, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdLessThan(String value) {
            addCriterion("T_OPERATE_LOG.DATA_ID <", value, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdLessThanOrEqualTo(String value) {
            addCriterion("T_OPERATE_LOG.DATA_ID <=", value, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdLike(String value) {
            addCriterion("T_OPERATE_LOG.DATA_ID like", value, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdNotLike(String value) {
            addCriterion("T_OPERATE_LOG.DATA_ID not like", value, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdIn(List<String> values) {
            addCriterion("T_OPERATE_LOG.DATA_ID in", values, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdNotIn(List<String> values) {
            addCriterion("T_OPERATE_LOG.DATA_ID not in", values, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdBetween(String value1, String value2) {
            addCriterion("T_OPERATE_LOG.DATA_ID between", value1, value2, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdNotBetween(String value1, String value2) {
            addCriterion("T_OPERATE_LOG.DATA_ID not between", value1, value2, "dataId");
            return (Criteria) this;
        }
    }

    /**
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}