package com.usehover.testerv2.models;

import com.usehover.testerv2.enums.StatusEnums;

public class ParsersInfoModel {
    private String parser_type, parser_action,parser_actionID, parser_category, parser_created, parser_sender, parser_regex;
    private StatusEnums statusEnums;

    public String getParser_type() {
        return parser_type;
    }

    public void setParser_type(String parser_type) {
        this.parser_type = parser_type;
    }

    public String getParser_action() {
        return parser_action;
    }

    public void setParser_action(String parser_action) {
        this.parser_action = parser_action;
    }

    public String getParser_actionID() {
        return parser_actionID;
    }

    public void setParser_actionID(String parser_actionID) {
        this.parser_actionID = parser_actionID;
    }

    public String getParser_category() {
        return parser_category;
    }

    public void setParser_category(String parser_category) {
        this.parser_category = parser_category;
    }

    public StatusEnums getStatusEnums() {
        return statusEnums;
    }

    public void setStatusEnums(StatusEnums statusEnums) {
        this.statusEnums = statusEnums;
    }

    public String getParser_created() {
        return parser_created;
    }

    public void setParser_created(String parser_created) {
        this.parser_created = parser_created;
    }

    public String getParser_sender() {
        return parser_sender;
    }

    public void setParser_sender(String parser_sender) {
        this.parser_sender = parser_sender;
    }

    public String getParser_regex() {
        return parser_regex;
    }

    public void setParser_regex(String parser_regex) {
        this.parser_regex = parser_regex;
    }
}
