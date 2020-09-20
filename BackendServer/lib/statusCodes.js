/**
 * Status codes list
 * These codes are used in Response classes to standardize the output response
 */
const statusCodes = {
	NOT_AUTHORIZED: {
		code: 401,
		message: 'Not authorized'
	},
	INVALID_TOKEN: {
		code: 401,
		message: 'Invalid token'
	},
	INVALID_CREDENTIALS: {
		code: 401,
		message: 'Invalid credentials'
	},
	SUCCESS: {
		code: 200,
		message: 'Success'
	},
	FORBIDDEN: {
		code: 403,
		message: 'Forbidden'
	},
	SERVER_ERROR: {
		code: 500,
		message: 'Internal Server Error'
	},
	BAD_INPUT: {
		code: 400,
		message: 'Bad Input'
    },
    NOT_FOUND: {
        code: 404,
        message: 'Page not found'
    }
};

module.exports = statusCodes;