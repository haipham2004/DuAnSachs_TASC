import { createSlice } from '@reduxjs/toolkit';

const initialState = {
    isAuthenticated: false,
    isLoading: true,
    user: {
        username: "", // String userName
        email: "", // String email
        phone: "", // String phone
        roles: [] // Array of Integer idRoles
    }
};

export const accountSlice = createSlice({
    name: 'account',
    initialState,
    reducers: {
        doLoginAction: (state, action) => {
            state.isAuthenticated = true;            
            state.isLoading = false;
            state.user = action.payload;
        },
        doGetAccountAction: (state, action) => {
            state.isAuthenticated = true;
            state.isLoading = false;
            state.user = action.payload;
        },
        doLogoutAction: (state) => {
            localStorage.removeItem('access_token');
            localStorage.removeItem('persist:root');
            state.isAuthenticated = false;
            state.user = initialState.user; // Reset to initial user state
        },
        doUpdateUserInfoAction: (state, action) => {
            state.user.username = action.payload.userName;
            state.user.phone = action.payload.phone;
            state.user.email = action.payload.email;
            state.user.roles = action.payload.roles;
        }
    },
    extraReducers: (builder) => {
        // Thêm các reducers nếu cần
    },
});

export const {
    doLoginAction,
    doGetAccountAction,
    doLogoutAction,
    doUpdateUserInfoAction
} = accountSlice.actions;

export default accountSlice.reducer;
