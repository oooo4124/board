package hello.board.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.board.domain.post.Post;
import hello.board.web.controller.BoardSearch;
import hello.board.dto.PostDto;
import hello.board.dto.QPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;

import java.util.List;

import static hello.board.domain.member.QMember.*;
import static hello.board.domain.post.QPost.*;
import static org.springframework.util.StringUtils.*;

public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<PostDto> boardSearch(BoardSearch boardSearch, Pageable pageable) {
        List<PostDto> contents = queryFactory
                .select(new QPostDto(
                        post.id.as("postId"),
                        post.postTitle,
                        post.postDate,
                        post.viewCount,
                        post.comments.size(),
                        member.id.as("memberId"),
                        member.username.as("userName")
                ))
                .from(post)
                .leftJoin(post.member, member)
                .where(
                        usernameSearch(boardSearch.getUsername()),
                        postTitleSearch(boardSearch.getPostTitle()),
                        postContentSearch(boardSearch.getPostContent()),
                        allSearch(boardSearch.getSearchContent())
                )
                .orderBy(post.postDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Post> countQuery = queryFactory
                .select(post)
                .from(post)
                .leftJoin(post.member, member)
                .where(
                        usernameSearch(boardSearch.getUsername()),
                        postTitleSearch(boardSearch.getPostTitle()),
                        postContentSearch(boardSearch.getPostContent()),
                        allSearch(boardSearch.getSearchContent())
                );

        // 페이지의 토탈 사이즈를 보고 조건 충족시 countQuery를 호출하지 않는다.
        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchCount);

    }

    private BooleanExpression allSearch(String searchContent) {
        return hasText(searchContent) ?
                post.member.username.contains(searchContent)
                        .or(post.postTitle.contains(searchContent))
                        .or(post.postContent.contains(searchContent)) : null;

    }

    private BooleanExpression usernameSearch(String username) {
        return hasText(username) ? post.member.username.contains(username) : null;
    }

    private BooleanExpression postTitleSearch(String postTitle) {
        return hasText(postTitle) ? post.postTitle.contains(postTitle) : null;
    }

    private BooleanExpression postContentSearch(String postContent) {
        return hasText(postContent) ? post.postContent.contains(postContent) : null;
    }
}
