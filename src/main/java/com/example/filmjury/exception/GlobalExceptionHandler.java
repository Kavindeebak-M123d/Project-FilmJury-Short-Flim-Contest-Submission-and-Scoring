package com.example.filmjury.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    private boolean isApiRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri != null && uri.startsWith("/api");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public Object handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (isApiRequest(request)) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
        }
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/entries";
    }

    @ExceptionHandler(DuplicateScoreException.class)
    public Object handleDuplicateScore(DuplicateScoreException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (isApiRequest(request)) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()), HttpStatus.CONFLICT);
        }
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/entries");
    }

    @ExceptionHandler(InvalidScoreException.class)
    public Object handleInvalidScore(InvalidScoreException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (isApiRequest(request)) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/entries");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        if (isApiRequest(request)) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
        }
        redirectAttributes.addFlashAttribute("errorMessage", message);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/entries");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (isApiRequest(request)) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/entries");
    }

    @ExceptionHandler(Exception.class)
    public Object handleGenericException(Exception ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String msg = ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred.";
        if (isApiRequest(request)) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        redirectAttributes.addFlashAttribute("errorMessage", msg);
        return "redirect:/";
    }
}
