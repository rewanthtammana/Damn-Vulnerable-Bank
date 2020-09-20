/**
 * Response constructor
 * This module is used across all routes to standardize the output response
 */
function Response(status, data) {
    this.status = status;
    this.data = data || "";
    this.toString = function() {
        return JSON.stringify({
            status: this.status,
            data: this.data
        });
    };
}

module.exports = Response;
