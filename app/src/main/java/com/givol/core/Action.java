package com.givol.core;

import com.givol.R;

public enum Action implements AbstractAction {

    Drawer {
        @Override
        public int getIconResId() {
            return R.drawable.ic_hamburger_menu;
        }

        @Override
        public int getTitleResId() {
            return R.string.action_drawer;
        }

        @Override
        public boolean isNavigation() {
            return true;
        }
    },

    BackWhite {
        @Override
        public int getIconResId() {
            return R.drawable.ic_back_white;
        }

        @Override
        public int getTitleResId() {
            return R.string.action_back;
        }

        @Override
        public boolean isNavigation() {
            return true;
        }
    },

    BackBlack {
        @Override
        public int getIconResId() {
            return R.drawable.ic_back_black;
        }

        @Override
        public int getTitleResId() {
            return R.string.action_back;
        }

        @Override
        public boolean isNavigation() {
            return true;
        }
    },

    CancelBlack {
        @Override
        public int getIconResId() {
            return R.drawable.ic_close_black;
        }

        @Override
        public int getTitleResId() {
            return R.string.action_cancel;
        }

        @Override
        public boolean isNavigation() {
            return true;
        }
    }
}
