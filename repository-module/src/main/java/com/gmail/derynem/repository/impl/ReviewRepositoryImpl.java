package com.gmail.derynem.repository.impl;

import com.gmail.derynem.repository.ReviewRepository;
import com.gmail.derynem.repository.exception.ReviewRepositoryException;
import com.gmail.derynem.repository.model.Review;
import com.gmail.derynem.repository.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.gmail.derynem.repository.constants.DataBaseVariables.OFFSET_LIMIT;

@Repository
public class ReviewRepositoryImpl extends GenericRepositoryImpl implements ReviewRepository {
    private final static Logger logger = LoggerFactory.getLogger(ReviewRepositoryImpl.class);

    @Override
    public List<Review> getReviewsWithOffset(Connection connection, int offset) {
        String sqlQuery = " SELECT * FROM T_REVIEW AS R LEFT JOIN T_USER AS U ON R.F_USER_ID= U.F_ID" +
                " WHERE R.F_DELETED = FALSE LIMIT ?,?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            return getReviews(offset, preparedStatement);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ReviewRepositoryException("error at method get reviews with offset at repository module|" + e.getMessage(), e);
        }
    }

    @Override
    public List<Review> getNotHiddenReviewsWithOffset(Connection connection, int offset) {
        String sqlQuery = " SELECT * FROM T_REVIEW AS R LEFT JOIN T_USER AS U ON R.F_USER_ID= U.F_ID" +
                " WHERE R.F_DELETED = FALSE AND R.F_HIDDEN = FALSE LIMIT ?,?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            return getReviews(offset, preparedStatement);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ReviewRepositoryException("error at method get not hidden reviews with offset at repository module|" + e.getMessage(), e);
        }
    }


    @Override
    public int getCountOfAllReviews(Connection connection) {
        String sqlQuery = " SELECT COUNT(F_ID) FROM T_REVIEW WHERE F_DELETED = FALSE";
        int count = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                count = resultSet.getInt(1);
                logger.info("count of all reviews in database = {}", count);
            }
            return count;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ReviewRepositoryException("error at method get count of reviews at repository module|" + e.getMessage(), e);
        }
    }

    @Override
    public int deleteReview(Connection connection, Long id) {
        String sqlQuery = "UPDATE T_REVIEW SET F_DELETED = TRUE WHERE F_ID =?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, id);
            int row = preparedStatement.executeUpdate();
            logger.info("rows change in database :{}", row);
            return row;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ReviewRepositoryException("error at method deleteReview at repository module" + e.getMessage(), e);
        }
    }

    @Override
    public int changeHiddenStatus(Connection connection, boolean condition, List<Long> ids) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE T_REVIEW SET F_HIDDEN =? WHERE ");
        for (int i = 0; i < ids.size(); i++) {
            if (i == 0) {
                stringBuilder.append(" F_ID = ").append(ids.get(i));
            } else {
                stringBuilder.append(" OR F_ID = ").append(ids.get(i));
            }
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(stringBuilder.toString())) {
            preparedStatement.setBoolean(1, condition);
            int rows = preparedStatement.executeUpdate();
            logger.info("reviews with new hidden status: {} - {}", condition, rows);
            return rows;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ReviewRepositoryException("error at method changeHiddenStatus at repository module" + e.getMessage(), e);
        }
    }

    @Override
    public int getCountOfNotHiddenReviews(Connection connection) {
        String sqlQuery = " SELECT COUNT(F_ID) FROM T_REVIEW WHERE F_DELETED = FALSE AND F_HIDDEN = FALSE ";
        int count = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                count = resultSet.getInt(1);
                logger.info("count of not hidden reviews in database = {}", count);
            }
            return count;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ReviewRepositoryException("error at method get count of not hidden reviews at repository module|"
                    + e.getMessage(), e);
        }
    }

    private List<Review> getReviews(int offset, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, offset);
        preparedStatement.setLong(2, OFFSET_LIMIT);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Review> reviews = new ArrayList<>();
            while (resultSet.next()) {
                reviews.add(getReview(resultSet));
            }
            return reviews;
        }
    }

    private Review getReview(ResultSet resultSet) throws SQLException {
        Review review = new Review();
        User user = new User();
        user.setName(resultSet.getString("F_NAME"));
        user.setSurName(resultSet.getString("F_SURNAME"));
        user.setMiddleName(resultSet.getString("F_MIDDLE_NAME"));
        user.setId(resultSet.getLong(7));
        review.setUser(user);
        review.setHidden(resultSet.getBoolean("F_HIDDEN"));
        review.setCreated(resultSet.getString("F_CREATED"));
        review.setDescription(resultSet.getString("F_DESCRIPTION"));
        review.setId(resultSet.getLong(1));
        review.setUserId(user.getId());
        return review;
    }
}
