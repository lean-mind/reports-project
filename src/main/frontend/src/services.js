export const reportService = () => {
    return {
        shouldRequestReport: (nextProps, state) => {
            let {location} = nextProps;
            return location && location.search &&
                   location.search !== state.latestReportFilters;
        }
    };
};
